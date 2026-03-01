package com.talenArena.SafeZone.models.geofencing;

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
