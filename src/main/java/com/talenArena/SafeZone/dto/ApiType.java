package com.talenArena.SafeZone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiType {
     LOCATION_VERIFICATION("locationVerification"),
    SIM_SWAP_DETECTION("simSwapDetection"),
    KYC_VERIFICATION("kycVerification");

    private String value;
}
