package com.example.cuentaMovimientos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoResponseDto {
    private Long id;
    private CuentaResponseDto cuentaId;
    private LocalDate fecha;
    private String tipoMovimiento;
    private double valor;
    private double saldo;
    private String clienteNombre;
}
