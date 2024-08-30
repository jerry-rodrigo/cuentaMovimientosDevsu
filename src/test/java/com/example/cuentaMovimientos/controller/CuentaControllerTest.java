package com.example.cuentaMovimientos.controller;

import com.example.cuentaMovimientos.dto.request.CuentaRequestDto;
import com.example.cuentaMovimientos.dto.response.CuentaResponseDto;
import com.example.cuentaMovimientos.exception.ResourceNotFoundException;
import com.example.cuentaMovimientos.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CuentaControllerTest {

    @InjectMocks
    private CuentaController cuentaController;

    @Mock
    private CuentaService cuentaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearCuenta() {
        CuentaRequestDto requestDto = new CuentaRequestDto();
        // Configura el DTO según sea necesario
        CuentaResponseDto responseDto = new CuentaResponseDto(1L, "123456", "Ahorros", 1000.0, 1000.0, true, 1L);

        when(cuentaService.crearCuenta(any(CuentaRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<CuentaResponseDto> response = cuentaController.crearCuenta(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    public void testActualizarCuenta() {
        Long id = 1L;
        CuentaRequestDto requestDto = new CuentaRequestDto();
        // Configura el DTO según sea necesario
        CuentaResponseDto responseDto = new CuentaResponseDto(id, "123456", "Ahorros", 1000.0, 1000.0, true, 1L);

        when(cuentaService.actualizarCuenta(eq(id), any(CuentaRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<CuentaResponseDto> response = cuentaController.actualizarCuenta(id, requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    public void testEliminarCuenta() {
        Long id = 1L;

        doNothing().when(cuentaService).eliminarCuenta(id);

        ResponseEntity<Void> response = cuentaController.eliminarCuenta(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cuentaService, times(1)).eliminarCuenta(id);
    }

    @Test
    public void testObtenerCuenta() {
        Long id = 1L;
        CuentaResponseDto responseDto = new CuentaResponseDto(id, "123456", "Ahorros", 1000.0, 1000.0, true, 1L);

        when(cuentaService.obtenerCuentaPorId(id)).thenReturn(responseDto);

        ResponseEntity<CuentaResponseDto> response = cuentaController.obtenerCuenta(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    public void testObtenerCuentaNotFound() {
        Long id = 1L;

        when(cuentaService.obtenerCuentaPorId(id)).thenThrow(new ResourceNotFoundException("Cuenta no encontrada"));

        ResponseEntity<CuentaResponseDto> response = cuentaController.obtenerCuenta(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testObtenerTodasLasCuentas() {
        CuentaResponseDto cuenta1 = new CuentaResponseDto(1L, "123456", "Ahorros", 1000.0, 1000.0, true, 1L);
        CuentaResponseDto cuenta2 = new CuentaResponseDto(2L, "654321", "Corriente", 2000.0, 2000.0, true, 2L);
        List<CuentaResponseDto> cuentas = Arrays.asList(cuenta1, cuenta2);

        when(cuentaService.obtenerTodasLasCuentas()).thenReturn(cuentas);

        ResponseEntity<List<CuentaResponseDto>> response = cuentaController.obtenerTodasLasCuentas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cuentas, response.getBody());
    }
}
