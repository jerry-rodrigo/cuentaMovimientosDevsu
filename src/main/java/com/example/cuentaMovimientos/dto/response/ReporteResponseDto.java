package com.example.cuentaMovimientos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) para representar la respuesta de un reporte.
 * Incluye información sobre el cliente y las cuentas asociadas al reporte.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReporteResponseDto {

    private String cliente;

    private List<CuentaReporteDto> cuentas;

    /**
     * Agrega una cuenta al reporte.
     *
     * <p>Si la lista de cuentas no está inicializada, se crea una nueva instancia. Luego, se agrega la cuenta proporcionada
     * a la lista.</p>
     *
     * @param cuentaReporteDto Objeto que contiene la información de la cuenta a agregar al reporte.
     */
    public void addCuenta(CuentaReporteDto cuentaReporteDto) {
        if (cuentas == null) {
            cuentas = new ArrayList<>();
        }
        cuentas.add(cuentaReporteDto);
    }
}
