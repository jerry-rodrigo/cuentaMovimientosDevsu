package com.example.cuentaMovimientos.service;

import com.example.common.dto.response.ClienteResponseDto;
import com.example.cuentaMovimientos.dto.response.*;
import com.example.cuentaMovimientos.entity.Cuenta;
import com.example.cuentaMovimientos.entity.Movimiento;
import com.example.cuentaMovimientos.repository.CuentaRepository;
import com.example.cuentaMovimientos.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la generación de reportes que consolidan la información de movimientos y cuentas.
 * <p>
 * Este servicio obtiene la información de cuentas y movimientos, y genera un reporte que incluye
 * detalles de las cuentas y sus movimientos dentro de un rango de fechas. Utiliza {@link WebClient}
 * para la comunicación con el microservicio de clientes.
 * </p>
 */
@Service
public class ReporteService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final WebClient webClient; // Usamos WebClient para comunicación entre microservicios

    /**
     * Constructor para inyectar las dependencias necesarias para la generación de reportes.
     *
     * @param cuentaRepository Repositorio para acceder a la información de cuentas.
     * @param movimientoRepository Repositorio para acceder a la información de movimientos.
     * @param webClientBuilder Builder para configurar WebClient.
     */
    @Autowired
    public ReporteService(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository, WebClient.Builder webClientBuilder) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    /**
     * Genera un reporte basado en una lista de IDs de cuentas y un rango de fechas.
     *
     * @param cuentasIds Lista de IDs de cuentas para incluir en el reporte.
     * @param fechaInicio Fecha de inicio del rango para el reporte.
     * @param fechaFin Fecha de fin del rango para el reporte.
     * @return {@link ReporteResponseDto} con la información del reporte generado.
     */
    public ReporteResponseDto generarReporte(List<Long> cuentasIds, LocalDate fechaInicio, LocalDate fechaFin) {
        ReporteResponseDto reporte = new ReporteResponseDto();

        for (Long cuentaId : cuentasIds) {
            Optional<Cuenta> cuentaOpt = cuentaRepository.findById(cuentaId);

            if (cuentaOpt.isPresent()) {
                Cuenta cuenta = cuentaOpt.get();

                // Usar clienteId en lugar de Cliente
                ClienteResponseDto cliente = obtenerClientePorId(cuenta.getClienteId());
                reporte.setCliente(cliente.getNombre());

                // Obtener movimientos en el rango de fechas
                List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaBetween(cuentaId, fechaInicio, fechaFin);
                List<MovimientoReporteDto> movimientosDto = convertirMovimientosAReporteDto(movimientos);

                // Establecer el saldo inicial y calcular el saldo actual
                double saldoInicial = cuenta.getSaldoInicial();
                double saldoActual = saldoInicial;

                for (Movimiento movimiento : movimientos) {
                    saldoActual += movimiento.getValor();
                }

                // Crear DTO de cuenta con movimientos
                CuentaReporteDto cuentaReporte = new CuentaReporteDto(
                        cuenta.getNumeroCuenta(),
                        cuenta.getTipoCuenta(),
                        saldoInicial,
                        cuenta.getEstado(),
                        movimientosDto
                );

                reporte.addCuenta(cuentaReporte);
            }
        }

        return reporte;
    }

    /**
     * Obtiene información del cliente desde el microservicio clientePersona.
     *
     * @param clienteId ID del cliente cuyo detalle se desea obtener.
     * @return {@link ClienteResponseDto} con la información del cliente.
     * @throws RuntimeException Si el cliente no se encuentra o hay un error en el servidor.
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
     * Convierte una lista de movimientos a una lista de DTOs para el reporte.
     *
     * @param movimientos Lista de {@link Movimiento} a convertir.
     * @return Lista de {@link MovimientoReporteDto} que representa los movimientos.
     */
    private List<MovimientoReporteDto> convertirMovimientosAReporteDto(List<Movimiento> movimientos) {
        return movimientos.stream().map(movimiento -> new MovimientoReporteDto(
                movimiento.getFecha(),
                movimiento.getTipoMovimiento(),
                movimiento.getValor(),
                movimiento.getSaldo()
        )).collect(Collectors.toList());
    }
}
