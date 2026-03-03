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

    // Datos comunes
    private String phoneNumber;

    // SIM Swap
    private Integer maxAge;

    // Location Verification
    private Double latitude;
    private Double longitude;
    private Integer radius;

}

