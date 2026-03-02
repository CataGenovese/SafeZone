package com.talenArena.SafeZone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Area {
    private String areaType;
    private Center center;
    private int radius;
}

