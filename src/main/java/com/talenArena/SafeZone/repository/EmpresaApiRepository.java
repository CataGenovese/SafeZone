package com.talenArena.SafeZone.repository;

import com.talenArena.SafeZone.entities.EmpresaApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaApiRepository extends JpaRepository<EmpresaApi, Long> {
    List<EmpresaApi> findByEmpresaId(Long empresaId);
    List<EmpresaApi> findByEmpresaIdAndHabilitada(Long empresaId, Boolean habilitada);
    Optional<EmpresaApi> findByEmpresaIdAndApiId(Long empresaId, Long apiId);

    @Query("SELECT ea FROM EmpresaApi ea JOIN FETCH ea.api WHERE ea.empresa.id = :empresaId AND ea.habilitada = true")
    List<EmpresaApi> findEnabledApisByEmpresaId(@Param("empresaId") Long empresaId);
}


