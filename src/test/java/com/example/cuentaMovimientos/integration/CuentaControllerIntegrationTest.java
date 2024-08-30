package com.example.cuentaMovimientos.integration;

import com.example.cuentaMovimientos.dto.request.CuentaRequestDto;
import com.example.cuentaMovimientos.dto.response.CuentaResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CuentaControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/cuentas";
    }

    @Test
    public void testCrearYObtenerCuenta() {
        // Crear cuenta
        CuentaRequestDto requestDto = new CuentaRequestDto();
        requestDto.setNumeroCuenta("123456");
        requestDto.setSaldoInicial(1000.0);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CuentaRequestDto> request = new HttpEntity<>(requestDto, headers);

        ResponseEntity<CuentaResponseDto> createResponse = restTemplate.exchange(baseUrl, HttpMethod.POST, request, CuentaResponseDto.class);

        assertEquals(201, createResponse.getStatusCodeValue());
        CuentaResponseDto createdCuenta = createResponse.getBody();
        assertEquals("123456", createdCuenta.getNumeroCuenta());

        // Obtener cuenta por ID
        String cuentaUrl = baseUrl + "/" + createdCuenta.getId();
        ResponseEntity<CuentaResponseDto> getResponse = restTemplate.exchange(cuentaUrl, HttpMethod.GET, null, CuentaResponseDto.class);

        assertEquals(200, getResponse.getStatusCodeValue());
        CuentaResponseDto cuenta = getResponse.getBody();
        assertEquals("123456", cuenta.getNumeroCuenta());
        assertEquals(1000.0, cuenta.getSaldoInicial());
    }
}
