package com.talenArena.SafeZone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SafeZoneScoreResponse {

    private int score;

    // true si todas las verificaciones pasaron (score == 100)
    private boolean approved;
}
