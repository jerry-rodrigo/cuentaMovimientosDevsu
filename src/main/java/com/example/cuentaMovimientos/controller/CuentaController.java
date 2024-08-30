package com.example.cuentaMovimientos.controller;

import com.example.cuentaMovimientos.dto.request.CuentaRequestDto;
import com.example.cuentaMovimientos.dto.response.CuentaResponseDto;
import com.example.cuentaMovimientos.exception.ResourceNotFoundException;
import com.example.cuentaMovimientos.service.CuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para manejar operaciones relacionadas con cuentas.
 * Proporciona endpoints para crear, obtener, actualizar y eliminar cuentas.
 */
@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    /**
     * Crea una nueva cuenta con los datos proporcionados.
     *
     * @param cuentaRequestDto Datos de la cuenta a crear.
     * @return ResponseEntity con la cuenta creada y el estado HTTP CREATED si la creación es exitosa,
     *         o con un mensaje de error y el estado HTTP BAD REQUEST si hay una excepción.
     */
    @PostMapping
    public ResponseEntity<CuentaResponseDto> crearCuenta(@RequestBody CuentaRequestDto cuentaRequestDto) {
        CuentaResponseDto cuenta = cuentaService.crearCuenta(cuentaRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cuenta);
    }

    /**
     * Actualiza una cuenta existente con los nuevos datos proporcionados.
     *
     * @param id ID de la cuenta a actualizar.
     * @param cuentaRequestDto Datos actualizados de la cuenta.
     * @return ResponseEntity con la cuenta actualizada y el estado HTTP OK si la actualización es exitosa,
     *         o con un mensaje de error y el estado HTTP NOT FOUND si la cuenta no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponseDto> actualizarCuenta(@PathVariable Long id, @RequestBody CuentaRequestDto cuentaRequestDto) {
        CuentaResponseDto cuenta = cuentaService.actualizarCuenta(id, cuentaRequestDto);
        return ResponseEntity.ok(cuenta);
    }

    /**
     * Elimina una cuenta específica por su ID.
     *
     * @param id ID de la cuenta a eliminar.
     * @return ResponseEntity con el estado HTTP NO CONTENT si la eliminación es exitosa,
     *         o con un mensaje de error y el estado HTTP NOT FOUND si la cuenta no existe.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCuenta(@PathVariable Long id) {
        cuentaService.eliminarCuenta(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene los detalles de una cuenta específica por su ID.
     *
     * @param id ID de la cuenta a buscar.
     * @return ResponseEntity con la cuenta encontrada y el estado HTTP OK si la cuenta existe,
     *         o con el estado HTTP NOT FOUND si la cuenta no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDto> obtenerCuenta(@PathVariable Long id) {
        try {
            CuentaResponseDto cuenta = cuentaService.obtenerCuentaPorId(id);
            return ResponseEntity.ok(cuenta);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Obtiene una lista de todas las cuentas.
     *
     * @return ResponseEntity con la lista de cuentas y el estado HTTP OK.
     */
    @GetMapping
    public ResponseEntity<List<CuentaResponseDto>> obtenerTodasLasCuentas() {
        List<CuentaResponseDto> cuentas = cuentaService.obtenerTodasLasCuentas();
        return ResponseEntity.ok(cuentas);
    }
}
