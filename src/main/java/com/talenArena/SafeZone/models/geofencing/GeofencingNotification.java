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
public class GeofencingNotification {
    @JsonProperty("subscriptionId")
    private String subscriptionId;
    private String type;
    @JsonProperty("eventTime")
    private String eventTime;
    private GeofencingDeviceInfo device;
    private GeofencingArea area;
}
