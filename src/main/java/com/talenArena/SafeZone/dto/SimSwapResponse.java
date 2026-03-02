package com.talenArena.SafeZone.dto;

import lombok.Data;

@Data
public class SimSwapResponse {
    private boolean swapped;
    private SwapDate timestamp; // opcional, fecha del último cambio
}

