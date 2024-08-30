package com.example.cuentaMovimientos.controller;

import com.example.cuentaMovimientos.dto.request.MovimientoRequestDto;
import com.example.cuentaMovimientos.dto.response.MovimientoResponseDto;
import com.example.cuentaMovimientos.exception.ResourceNotFoundException;
import com.example.cuentaMovimientos.exception.SaldoInsuficienteException;
import com.example.cuentaMovimientos.service.MovimientoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para manejar operaciones relacionadas con los movimientos.
 * Proporciona endpoints para crear, obtener, actualizar y eliminar movimientos.
 */
@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    /**
     * Crea un nuevo movimiento con los datos proporcionados.
     *
     * @param movimientoRequestDto Datos del movimiento a crear.
     * @return ResponseEntity con el movimiento creado y el estado HTTP CREATED,
     *         o con un mensaje de error y el estado HTTP BAD REQUEST si ocurre una excepción.
     */
    @PostMapping
    public ResponseEntity<MovimientoResponseDto> crearMovimiento(@RequestBody MovimientoRequestDto movimientoRequestDto) {
        try {
            MovimientoResponseDto response = movimientoService.crearMovimiento(movimientoRequestDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (SaldoInsuficienteException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtiene los detalles de un movimiento específico por su ID.
     *
     * @param id ID del movimiento a buscar.
     * @return ResponseEntity con el movimiento encontrado y el estado HTTP OK,
     *         o con un mensaje de error y el estado HTTP NOT FOUND si el movimiento no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponseDto> obtenerMovimiento(@PathVariable Long id) {
        try {
            MovimientoResponseDto movimiento = movimientoService.obtenerMovimientoPorId(id);
            return ResponseEntity.ok(movimiento);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Obtiene una lista de movimientos filtrados por fecha y cliente.
     *
     * @param fechaInicio Fecha de inicio del rango.
     * @param fechaFin Fecha de fin del rango.
     * @param clienteId ID del cliente.
     * @return ResponseEntity con la lista de movimientos y el estado HTTP OK,
     *         o con un mensaje de error y el estado HTTP BAD REQUEST si ocurre una excepción.
     */
    @GetMapping("/listado")
    public ResponseEntity<List<MovimientoResponseDto>> obtenerMovimientosPorFechaYCliente(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            @RequestParam Long clienteId) {
        try {
            List<MovimientoResponseDto> movimientos = movimientoService.obtenerMovimientosPorFechaYCliente(fechaInicio, fechaFin, clienteId);
            return ResponseEntity.ok(movimientos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Actualiza un movimiento existente con los nuevos datos proporcionados.
     *
     * @param id ID del movimiento a actualizar.
     * @param movimientoRequestDto Datos actualizados del movimiento.
     * @return ResponseEntity con el movimiento actualizado y el estado HTTP OK,
     *         o con un mensaje de error y el estado HTTP NOT FOUND si el movimiento no existe,
     *         o con un mensaje de error y el estado HTTP BAD REQUEST si ocurre una excepción.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoResponseDto> actualizarMovimiento(@PathVariable Long id, @RequestBody MovimientoRequestDto movimientoRequestDto) {
        try {
            MovimientoResponseDto updatedMovimiento = movimientoService.actualizarMovimiento(id, movimientoRequestDto);
            return ResponseEntity.ok(updatedMovimiento);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (SaldoInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Elimina un movimiento específico por su ID.
     *
     * @param id ID del movimiento a eliminar.
     * @return ResponseEntity con un mensaje de éxito y el estado HTTP NO CONTENT si la eliminación es exitosa,
     *         o con un mensaje de error y el estado HTTP NOT FOUND si el movimiento no existe.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMovimiento(@PathVariable Long id) {
        try {
            movimientoService.eliminarMovimiento(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Movimiento eliminado exitosamente.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
