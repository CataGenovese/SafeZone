package com.talenArena.SafeZone.repository;

import com.talenArena.SafeZone.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByEmpresaId(Long empresaId);

    @Query(value = "SELECT datos->>'nombre_interno' FROM cliente WHERE id = :id", nativeQuery = true)
    String findNombreInterno(@Param("id") Long id);

    // Campo anidado
    @Query(value = "SELECT datos->'direccion'->>'ciudad' FROM cliente WHERE id = :id", nativeQuery = true)
    String findCiudad(@Param("id") Long id);

    // Misma consulta con ruta:
    @Query(value = "SELECT datos#>>'{direccion,ciudad}' FROM cliente WHERE id = :id", nativeQuery = true)
    String findCiudadRuta(@Param("id") Long id);

    // Array (primer tag)
    @Query(value = "SELECT (datos->'tags'->>0) FROM cliente WHERE id = :id", nativeQuery = true)
    String findPrimerTag(@Param("id") Long id);

}

