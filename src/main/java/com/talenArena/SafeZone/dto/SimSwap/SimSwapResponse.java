package com.talenArena.SafeZone.dto.SimSwap;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimSwapResponse {
    private boolean swapped;
    private SwapDate timestamp;
}

