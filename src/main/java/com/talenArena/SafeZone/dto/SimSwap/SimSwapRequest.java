package com.talenArena.SafeZone.dto.SimSwap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimSwapRequest {
    private String phoneNumber;
    private Integer maxAge; // opcional, edad máxima del cambio de SIM en horas
}
