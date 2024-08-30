package com.example.cuentaMovimientos.controller;

import com.example.cuentaMovimientos.dto.response.ReporteResponseDto;
import com.example.cuentaMovimientos.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para manejar operaciones relacionadas con reportes.
 * Proporciona un endpoint para generar un reporte basado en un conjunto de cuentas y un rango de fechas.
 */
@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    /**
     * Constructor para inyectar el servicio de reportes.
     *
     * @param reporteService Servicio utilizado para generar reportes.
     */
    @Autowired
    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    /**
     * Obtiene un reporte basado en los IDs de cuentas y un rango de fechas.
     *
     * @param cuentasIds Lista de IDs de cuentas para incluir en el reporte.
     * @param fechaInicio Fecha de inicio del rango para el reporte.
     * @param fechaFin Fecha de fin del rango para el reporte.
     * @return ReporteResponseDto con la información del reporte generado.
     */
    @GetMapping
    public ReporteResponseDto obtenerReporte(
            @RequestParam List<Long> cuentasIds,
            @RequestParam("fecha_inicio") LocalDate fechaInicio,
            @RequestParam("fecha_fin") LocalDate fechaFin) {

        return reporteService.generarReporte(cuentasIds, fechaInicio, fechaFin);
    }
}
