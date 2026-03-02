package com.talenArena.SafeZone.repository;

import com.talenArena.SafeZone.entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByEmail(String email);
    List<Empleado> findByEmpresaId(Long empresaId);
    List<Empleado> findByEmpresaIdAndActivo(Long empresaId, Boolean activo);
}

