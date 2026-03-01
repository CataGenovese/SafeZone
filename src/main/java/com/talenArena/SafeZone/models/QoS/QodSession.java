package com.talenArena.SafeZone.models.QoS;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QodSession {

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("device")
    private Device device;

    @JsonProperty("service_ipv4")
    private String serviceIpv4;

    @JsonProperty("service_ipv6")
    private String serviceIpv6;

    @JsonProperty("qos_profile")
    private String qosProfile;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("started_at")
    private LocalDateTime startedAt;

    @JsonProperty("expires_at")
    private LocalDateTime expiresAt;

    @JsonProperty("status")
    private String status;
}

