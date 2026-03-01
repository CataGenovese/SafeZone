package com.talenArena.SafeZone.models.geofencing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeofencingArea {
    private String areaType;
    private GeofencingCircle circle;
}
