package com.example.cuentaMovimientos.controller;

import com.example.cuentaMovimientos.controller.MovimientoController;
import com.example.cuentaMovimientos.dto.request.MovimientoRequestDto;
import com.example.cuentaMovimientos.dto.response.MovimientoResponseDto;
import com.example.cuentaMovimientos.exception.ResourceNotFoundException;
import com.example.cuentaMovimientos.exception.SaldoInsuficienteException;
import com.example.cuentaMovimientos.service.MovimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MovimientoControllerTest {

    @InjectMocks
    private MovimientoController movimientoController;

    @Mock
    private MovimientoService movimientoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearMovimiento() {
        MovimientoRequestDto requestDto = new MovimientoRequestDto();
        // Configura el DTO según sea necesario
        MovimientoResponseDto responseDto = new MovimientoResponseDto(1L, null, LocalDate.now(), "Deposito", 500.0, 1000.0, "Cliente 1");

        when(movimientoService.crearMovimiento(any(MovimientoRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<MovimientoResponseDto> response = movimientoController.crearMovimiento(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    public void testCrearMovimientoSaldoInsuficiente() {
        MovimientoRequestDto requestDto = new MovimientoRequestDto();
        // Configura el DTO según sea necesario

        when(movimientoService.crearMovimiento(any(MovimientoRequestDto.class))).thenThrow(new SaldoInsuficienteException("Saldo insuficiente"));

        ResponseEntity<MovimientoResponseDto> response = movimientoController.crearMovimiento(requestDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testObtenerMovimiento() {
        Long id = 1L;
        MovimientoResponseDto responseDto = new MovimientoResponseDto(id, null, LocalDate.now(), "Deposito", 500.0, 1000.0, "Cliente 1");

        when(movimientoService.obtenerMovimientoPorId(id)).thenReturn(responseDto);

        ResponseEntity<MovimientoResponseDto> response = movimientoController.obtenerMovimiento(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    public void testObtenerMovimientoNotFound() {
        Long id = 1L;

        when(movimientoService.obtenerMovimientoPorId(id)).thenThrow(new ResourceNotFoundException("Movimiento no encontrado"));

        ResponseEntity<MovimientoResponseDto> response = movimientoController.obtenerMovimiento(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testObtenerMovimientosPorFechaYCliente() {
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";
        Long clienteId = 1L;
        MovimientoResponseDto movimiento1 = new MovimientoResponseDto(1L, null, LocalDate.now(), "Deposito", 500.0, 1000.0, "Cliente 1");
        MovimientoResponseDto movimiento2 = new MovimientoResponseDto(2L, null, LocalDate.now(), "Retiro", 200.0, 800.0, "Cliente 2");
        List<MovimientoResponseDto> movimientos = Arrays.asList(movimiento1, movimiento2);

        when(movimientoService.obtenerMovimientosPorFechaYCliente(fechaInicio, fechaFin, clienteId)).thenReturn(movimientos);

        ResponseEntity<List<MovimientoResponseDto>> response = movimientoController.obtenerMovimientosPorFechaYCliente(fechaInicio, fechaFin, clienteId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movimientos, response.getBody());
    }

    @Test
    public void testActualizarMovimiento() {
        Long id = 1L;
        MovimientoRequestDto requestDto = new MovimientoRequestDto();
        MovimientoResponseDto responseDto = new MovimientoResponseDto(id, null, LocalDate.now(), "Deposito", 500.0, 1000.0, "Cliente 1");

        when(movimientoService.actualizarMovimiento(eq(id), any(MovimientoRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<MovimientoResponseDto> response = movimientoController.actualizarMovimiento(id, requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    public void testActualizarMovimientoNotFound() {
        Long id = 1L;
        MovimientoRequestDto requestDto = new MovimientoRequestDto();

        when(movimientoService.actualizarMovimiento(eq(id), any(MovimientoRequestDto.class))).thenThrow(new ResourceNotFoundException("Movimiento no encontrado"));

        ResponseEntity<MovimientoResponseDto> response = movimientoController.actualizarMovimiento(id, requestDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testEliminarMovimiento() {
        Long id = 1L;

        doNothing().when(movimientoService).eliminarMovimiento(id);

        ResponseEntity<String> response = movimientoController.eliminarMovimiento(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Movimiento eliminado exitosamente.", response.getBody());
    }

    @Test
    public void testEliminarMovimientoNotFound() {
        Long id = 1L;

        doThrow(new ResourceNotFoundException("Movimiento no encontrado")).when(movimientoService).eliminarMovimiento(id);

        ResponseEntity<String> response = movimientoController.eliminarMovimiento(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Movimiento no encontrado", response.getBody());
    }
}