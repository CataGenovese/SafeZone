package com.talenArena.SafeZone.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateQodSessionRequest {
    private Device device;
    private String serviceIpv4;
    private String serviceIpv6;
    private String qosProfile;
    private Integer duration;
}

