package com.talenArena.SafeZone.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.talenArena.SafeZone.models.geofencing.GeofencingSubscriptionConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeofencingSubscription {

    private String protocol;
    private String sink;
    private List<String> types;
    private GeofencingSubscriptionConfig config;

    @JsonProperty("subscriptionId")
    private String subscriptionId;

    @JsonProperty("startsAt")
    private String startsAt;

    @JsonProperty("expiresAt")
    private String expiresAt;
}

