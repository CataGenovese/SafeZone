package com.talenArena.SafeZone.repository;

import com.talenArena.SafeZone.entities.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiRepository extends JpaRepository<Api, Long> {
    Optional<Api> findByNombre(String nombre);
    List<Api> findByTipo(String tipo);
}

