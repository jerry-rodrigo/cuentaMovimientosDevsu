package com.example.cuentaMovimientos.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CuentaRequestDto {
    private String numeroCuenta;
    private String tipoCuenta;
    private double saldoInicial;
    private Boolean estado;
    private Long clienteId;
}
