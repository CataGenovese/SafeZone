package com.talenArena.SafeZone.models;

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
public class CreateGeofencingSubscriptionRequest {

    @Builder.Default
    private String protocol = "HTTP";

    private String sink;
    private List<String> types;
    private GeofencingSubscriptionConfig config;
}
