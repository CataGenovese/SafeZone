package com.talenArena.SafeZone.dto.Location;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationVerificationRequest {
    private Device device;
    private Area area;
}
