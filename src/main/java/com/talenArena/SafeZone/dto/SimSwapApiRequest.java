package com.talenArena.SafeZone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimSwapApiRequest {
    @Builder.Default
    private DeviceInfo device = new DeviceInfo();
    private Integer maxAge;

    public SimSwapApiRequest(String phoneNumber) {
        this.device = new DeviceInfo(phoneNumber);
    }
}

