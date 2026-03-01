package com.talenArena.SafeZone.models.geofencing;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeofencingSubscriptionDetail {
    private GeofencingDeviceInfo device;
    private GeofencingArea area;
    @JsonProperty("subscriptionMaxEvents")
    private Integer subscriptionMaxEvents;
    @JsonProperty("subscriptionExpireTime")
    private String subscriptionExpireTime;
}
