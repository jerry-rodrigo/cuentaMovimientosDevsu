package com.example.cuentaMovimientos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CuentaReporteDto {
    private String numeroCuenta;
    private String tipoCuenta;
    private double saldoInicial;
    private Boolean estado;
    private List<MovimientoReporteDto> movimientos;
}
