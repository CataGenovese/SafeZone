package com.talenArena.SafeZone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataMatchResult {
    private boolean executed;
    private boolean match;
    private String storedData;
    private String requestData;
    private String error;
}

