package com.talenArena.SafeZone.dto.Auth;

import com.talenArena.SafeZone.dto.Kyc.KycMatchResponse;
import com.talenArena.SafeZone.dto.Location.LocationLastResponse;
import com.talenArena.SafeZone.dto.SimSwap.SimSwapResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private LocationLastResponse locationLastResponse;
    private KycMatchResponse kycMatchResponse;
    private SimSwapResponse simSwapResponse;
}
