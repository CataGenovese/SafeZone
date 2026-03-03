package com.talenArena.SafeZone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompanyType {
    TECH("Tech"),
    FINANCE("Finance"),
    HEALTHCARE("Healthcare"),
    EDUCATION("Education"),
    RETAIL("Retail"),
    MANUFACTURING("Manufacturing"),
    OTHER("Other");

    private String value;
}
