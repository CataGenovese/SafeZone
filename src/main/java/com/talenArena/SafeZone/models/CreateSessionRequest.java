package com.talenArena.SafeZone.models;

import lombok.Data;

@Data
public class CreateSessionRequest {
    private String publicAddress;
    private String serviceIpv4;
    private String profile;
    private Integer duration;
}

