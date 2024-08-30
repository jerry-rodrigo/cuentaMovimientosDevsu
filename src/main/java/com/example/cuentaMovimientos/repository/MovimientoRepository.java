package com.example.cuentaMovimientos.repository;

import com.example.cuentaMovimientos.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para manejar operaciones de persistencia relacionadas con la entidad {@link Movimiento}.
 * <p>
 * Esta interfaz extiende {@link JpaRepository} y proporciona métodos adicionales para realizar consultas
 * específicas sobre la entidad {@link Movimiento}. Utiliza la implementación proporcionada por Spring Data JPA
 * para realizar operaciones CRUD y consultas personalizadas.
 * </p>
 */
@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaIdAndFechaBetween(Long cuentaId, LocalDate fechaInicio, LocalDate fechaFin);
}
