package com.example.cuentaMovimientos.service;

import com.example.common.dto.response.ClienteResponseDto;
import com.example.cuentaMovimientos.dto.request.MovimientoRequestDto;
import com.example.cuentaMovimientos.dto.response.CuentaResponseDto;
import com.example.cuentaMovimientos.dto.response.MovimientoResponseDto;
import com.example.cuentaMovimientos.entity.Cuenta;
import com.example.cuentaMovimientos.entity.Movimiento;
import com.example.cuentaMovimientos.exception.ResourceNotFoundException;
import com.example.cuentaMovimientos.exception.SaldoInsuficienteException;
import com.example.cuentaMovimientos.repository.CuentaRepository;
import com.example.cuentaMovimientos.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para manejar operaciones relacionadas con los movimientos.
 * Proporciona métodos para crear, obtener, actualizar y eliminar movimientos.
 */
@Service
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final WebClient webClient; // Usamos WebClient para comunicación entre microservicios

    @Autowired
    public MovimientoService(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository, WebClient.Builder webClientBuilder) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    /**
     * Crea un nuevo movimiento con los datos proporcionados.
     *
     * @param movimientoRequestDto Datos del movimiento a crear.
     * @return MovimientoResponseDto con los detalles del movimiento creado.
     * @throws SaldoInsuficienteException si el saldo no es suficiente para realizar el movimiento.
     */
    @Transactional
    public MovimientoResponseDto crearMovimiento(MovimientoRequestDto movimientoRequestDto) {
        // Encontrar la cuenta
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(movimientoRequestDto.getNumeroCuenta())
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        // Calcular el nuevo saldo con el saldo actual
        double nuevoSaldo = cuenta.getSaldoActual() + movimientoRequestDto.getMovimiento();

        if (nuevoSaldo < 0) {
            throw new SaldoInsuficienteException("Saldo no disponible");
        }

        // Crear y guardar el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(movimientoRequestDto.getFecha());
        movimiento.setTipoMovimiento(movimientoRequestDto.getTipo());
        movimiento.setValor(movimientoRequestDto.getMovimiento());
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);
        Movimiento savedMovimiento = movimientoRepository.save(movimiento);

        // Actualizar el saldo actual de la cuenta
        cuenta.setSaldoActual(nuevoSaldo);
        cuentaRepository.save(cuenta);

        return convertToDto(savedMovimiento);
    }

    /**
     * Obtiene los detalles de un movimiento específico por su ID.
     *
     * @param id ID del movimiento a buscar.
     * @return MovimientoResponseDto con los detalles del movimiento encontrado.
     * @throws ResourceNotFoundException si el movimiento no se encuentra.
     */
    public MovimientoResponseDto obtenerMovimientoPorId(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));
        return convertToDto(movimiento);
    }

    /**
     * Obtiene una lista de movimientos filtrados por fecha y cliente.
     *
     * @param fechaInicio Fecha de inicio del rango.
     * @param fechaFin Fecha de fin del rango.
     * @param clienteId ID del cliente.
     * @return Lista de MovimientoResponseDto con los movimientos encontrados.
     * @throws ResourceNotFoundException si la cuenta o cliente no se encuentran.
     */
    public List<MovimientoResponseDto> obtenerMovimientosPorFechaYCliente(String fechaInicio, String fechaFin, Long clienteId) {
        // Obtener la cuenta del cliente
        Cuenta cuenta = cuentaRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada para el cliente"));

        // Convertir fechas de String a LocalDate
        LocalDate fechaInicioDate = LocalDate.parse(fechaInicio);
        LocalDate fechaFinDate = LocalDate.parse(fechaFin);

        // Buscar movimientos
        List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaBetween(cuenta.getId(), fechaInicioDate, fechaFinDate);
        List<MovimientoResponseDto> movimientosDto = movimientos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // Obtener el nombre del cliente
        ClienteResponseDto cliente = obtenerClientePorId(cuenta.getClienteId());
        for (MovimientoResponseDto dto : movimientosDto) {
            dto.setClienteNombre(cliente.getNombre());
        }

        return movimientosDto;
    }

    /**
     * Actualiza un movimiento existente con los nuevos datos proporcionados.
     *
     * @param id ID del movimiento a actualizar.
     * @param movimientoRequestDto Datos actualizados del movimiento.
     * @return MovimientoResponseDto con los detalles del movimiento actualizado.
     * @throws ResourceNotFoundException si el movimiento no se encuentra.
     * @throws SaldoInsuficienteException si el saldo no es suficiente para realizar el movimiento.
     */
    @Transactional
    public MovimientoResponseDto actualizarMovimiento(Long id, MovimientoRequestDto movimientoRequestDto) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));

        // Verificar y actualizar la cuenta relacionada
        Cuenta cuenta = movimiento.getCuenta();
        double nuevoSaldo = cuenta.getSaldoActual() + movimientoRequestDto.getMovimiento() - movimiento.getValor();

        if (nuevoSaldo < 0) {
            throw new SaldoInsuficienteException("Saldo no disponible");
        }

        // Actualizar movimiento
        movimiento.setFecha(movimientoRequestDto.getFecha());
        movimiento.setTipoMovimiento(movimientoRequestDto.getTipo());
        movimiento.setValor(movimientoRequestDto.getMovimiento());
        movimiento.setSaldo(nuevoSaldo);
        Movimiento updatedMovimiento = movimientoRepository.save(movimiento);

        // Actualizar el saldo actual de la cuenta
        cuenta.setSaldoActual(nuevoSaldo);
        cuentaRepository.save(cuenta);

        return convertToDto(updatedMovimiento);
    }

    /**
     * Elimina un movimiento específico por su ID.
     *
     * @param id ID del movimiento a eliminar.
     * @throws ResourceNotFoundException si el movimiento no se encuentra.
     */
    @Transactional
    public void eliminarMovimiento(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));

        // Actualizar el saldo de la cuenta
        Cuenta cuenta = movimiento.getCuenta();
        double nuevoSaldo = cuenta.getSaldoActual() - movimiento.getValor();
        cuenta.setSaldoActual(nuevoSaldo);
        cuentaRepository.save(cuenta);

        movimientoRepository.deleteById(id);
    }

    /**
     * Obtiene la información de un cliente desde el microservicio clientePersona.
     *
     * @param clienteId ID del cliente cuya información se desea obtener.
     * @return ClienteResponseDto con los detalles del cliente obtenido.
     * @throws RuntimeException si ocurre un error al intentar obtener el cliente (errores 4xx o 5xx).
     */
    private ClienteResponseDto obtenerClientePorId(Long clienteId) {
        return webClient.get()
                .uri("/clientes/{id}", clienteId)  // Endpoint del microservicio clientePersona
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> Mono.error(new RuntimeException("Cliente no encontrado")))
                .onStatus(status -> status.is5xxServerError(), response -> Mono.error(new RuntimeException("Error en el servidor de clientePersona")))
                .bodyToMono(ClienteResponseDto.class)
                .block();  // Bloqueamos para obtener el resultado sincrónicamente (podrías hacerlo de forma reactiva también)
    }


    /**
     * Convierte una entidad Movimiento en un DTO MovimientoResponseDto.
     *
     * @param movimiento Entidad Movimiento que se desea convertir.
     * @return MovimientoResponseDto con los datos del movimiento.
     */
    private MovimientoResponseDto convertToDto(Movimiento movimiento) {
        MovimientoResponseDto dto = new MovimientoResponseDto();
        dto.setId(movimiento.getId());
        dto.setFecha(movimiento.getFecha());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setValor(movimiento.getValor());
        dto.setSaldo(movimiento.getSaldo());

        // Obtener la cuenta asociada y mapearla
        Cuenta cuenta = movimiento.getCuenta();
        dto.setCuentaId(cuenta != null ? convertCuentaToDto(cuenta) : null);
        return dto;
    }


    /**
     * Convierte una entidad Cuenta en un DTO CuentaResponseDto.
     *
     * @param cuenta Entidad Cuenta que se desea convertir.
     * @return CuentaResponseDto con los datos de la cuenta.
     */
    private CuentaResponseDto convertCuentaToDto(Cuenta cuenta) {
        CuentaResponseDto dto = new CuentaResponseDto();
        dto.setId(cuenta.getId());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setTipoCuenta(cuenta.getTipoCuenta());
        dto.setSaldoInicial(cuenta.getSaldoInicial());
        dto.setSaldoActual(cuenta.getSaldoActual());
        dto.setEstado(cuenta.getEstado());
        dto.setClienteId(cuenta.getClienteId());
        return dto;
    }
}