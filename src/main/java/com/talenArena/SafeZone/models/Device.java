package com.talenArena.SafeZone.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @JsonProperty("ipv4_address")
    private DeviceIpv4Addr ipv4Address;

    @JsonProperty("ipv6_address")
    private String ipv6Address;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("network_access_identifier")
    private String networkAccessIdentifier;
}

