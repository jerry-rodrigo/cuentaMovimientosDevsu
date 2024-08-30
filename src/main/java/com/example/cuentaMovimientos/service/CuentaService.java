package com.example.cuentaMovimientos.service;

import com.example.cuentaMovimientos.dto.request.CuentaRequestDto;
import com.example.cuentaMovimientos.dto.response.CuentaResponseDto;
import com.example.cuentaMovimientos.entity.Cuenta;
import com.example.cuentaMovimientos.exception.ResourceNotFoundException;
import com.example.cuentaMovimientos.repository.CuentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para manejar operaciones relacionadas con las cuentas.
 * Proporciona métodos para crear, obtener, actualizar y eliminar cuentas.
 */
@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    /**
     * Crea una nueva cuenta con los datos proporcionados.
     *
     * @param cuentaRequestDto Datos de la cuenta a crear.
     * @return CuentaResponseDto con la cuenta creada.
     */
    public CuentaResponseDto crearCuenta(CuentaRequestDto cuentaRequestDto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(cuentaRequestDto.getNumeroCuenta());
        cuenta.setTipoCuenta(cuentaRequestDto.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaRequestDto.getSaldoInicial());
        cuenta.setSaldoActual(cuentaRequestDto.getSaldoInicial());
        cuenta.setEstado(cuentaRequestDto.getEstado());
        cuenta.setClienteId(cuentaRequestDto.getClienteId());
        Cuenta savedCuenta = cuentaRepository.save(cuenta);
        return convertToDto(savedCuenta);
    }

    /**
     * Actualiza una cuenta existente con los nuevos datos proporcionados.
     *
     * @param id ID de la cuenta a actualizar.
     * @param cuentaRequestDto Datos actualizados de la cuenta.
     * @return CuentaResponseDto con la cuenta actualizada.
     */
    public CuentaResponseDto actualizarCuenta(Long id, CuentaRequestDto cuentaRequestDto) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        cuenta.setNumeroCuenta(cuentaRequestDto.getNumeroCuenta());
        cuenta.setTipoCuenta(cuentaRequestDto.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaRequestDto.getSaldoInicial());
        cuenta.setEstado(cuentaRequestDto.getEstado());
        Cuenta updatedCuenta = cuentaRepository.save(cuenta);
        return convertToDto(updatedCuenta);
    }

    /**
     * Elimina una cuenta específica por su ID.
     *
     * @param id ID de la cuenta a eliminar.
     */
    public void eliminarCuenta(Long id) {
        cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        cuentaRepository.deleteById(id);
    }

    /**
     * Obtiene los detalles de una cuenta específica por su ID.
     *
     * @param id ID de la cuenta a buscar.
     * @return CuentaResponseDto con la cuenta encontrada.
     */
    public CuentaResponseDto obtenerCuentaPorId(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        return convertToDto(cuenta);
    }

    /**
     * Obtiene una lista de todas las cuentas.
     *
     * @return List<CuentaResponseDto> con todas las cuentas.
     */
    public List<CuentaResponseDto> obtenerTodasLasCuentas() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        return cuentas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad Cuenta a un DTO CuentaResponseDto.
     *
     * @param cuenta Entidad Cuenta.
     * @return CuentaResponseDto con los datos de la cuenta.
     */
    private CuentaResponseDto convertToDto(Cuenta cuenta) {
        CuentaResponseDto dto = new CuentaResponseDto();
        dto.setId(cuenta.getId());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setTipoCuenta(cuenta.getTipoCuenta());
        dto.setSaldoInicial(cuenta.getSaldoInicial());
        dto.setEstado(cuenta.getEstado());
        dto.setClienteId(cuenta.getClienteId());
        return dto;
    }
}
