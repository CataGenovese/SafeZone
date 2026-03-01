package com.talenArena.SafeZone.models.QoS;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceIpv4Addr {

    @JsonProperty("public_address")
    private String publicAddress;

    @JsonProperty("private_address")
    private String privateAddress;

    @JsonProperty("public_port")
    private Integer publicPort;
}

