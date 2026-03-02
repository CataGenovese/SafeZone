package com.talenArena.SafeZone.dto.Location;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationLastRequest {
    private Device device;
    private int maxAge;
}

