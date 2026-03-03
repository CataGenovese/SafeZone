package com.talenArena.SafeZone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SafeZoneResponse {

    private boolean success;
    private String message;

    // Resultados individuales
    private DataMatchResult dataMatch;
    private SimSwapResult simSwap;
    private LocationResult location;
    private KycResult kyc;
}
