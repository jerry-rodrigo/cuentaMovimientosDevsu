package com.example.cuentaMovimientos.repository;

import com.example.cuentaMovimientos.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para manejar operaciones de persistencia relacionadas con la entidad {@link Cuenta}.
 * <p>
 * Esta interfaz extiende {@link JpaRepository} y proporciona métodos adicionales para realizar consultas
 * específicas sobre la entidad {@link Cuenta}. Utiliza la implementación proporcionada por Spring Data JPA
 * para realizar operaciones CRUD y consultas personalizadas.
 * </p>
 */
@Repository
public interface  CuentaRepository extends JpaRepository<Cuenta, Long> {

    Optional<Cuenta> findByClienteId(Long clienteId);

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
}
