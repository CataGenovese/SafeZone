package com.talenArena.SafeZone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonInfoResponse {
    private String phoneNumber;
    private String idDocument;
    private String name;
    private String givenName;
    private String familyName;
    private String nameKanaHankaku;
    private String nameKanaZenkaku;
    private String middleNames;
    private String familyNameAtBirth;
    private String address;
    private String streetName;
    private String streetNumber;
    private String postalCode;
    private String region;
    private String locality;
    private String country;
    private String houseNumberExtension;
    private String birthdate;
    private String email;
    private String gender;
    private String cityOfBirth;
    private String countryOfBirth;
    private String nationality;
}

