package com.talenArena.SafeZone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SafeZoneRequest {

    // ID del cliente en BBDD para verificar datos
    private Long clienteId;

    // ID de la empresa para consultar APIs habilitadas
    private Long empresaId;

    // Datos comunes
    private String phoneNumber;

    // SIM Swap
    private Integer maxAge;

    // Location Verification
    private Double latitude;
    private Double longitude;
    private Integer radius;

}

