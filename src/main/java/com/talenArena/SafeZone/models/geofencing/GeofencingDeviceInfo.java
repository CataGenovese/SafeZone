package com.talenArena.SafeZone.models.geofencing;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeofencingDeviceInfo {
    private String phoneNumber;
    private String networkAccessIdentifier;
    @JsonProperty("ipv4Address")
    private Map<String, Object> ipv4Address;
    @JsonProperty("ipv6Address")
    private String ipv6Address;
}
