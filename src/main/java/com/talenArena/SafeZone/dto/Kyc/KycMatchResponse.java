package com.talenArena.SafeZone.dto.Kyc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KycMatchResponse {
    private String idDocumentMatch;
    private String nameMatch;
    private String givenNameMatch;
    private String familyNameMatch;
    private String nameKanaHankakuMatch;
    private String nameKanaZenkakuMatch;
    private String middleNamesMatch;
    private String familyNameAtBirthMatch;
    private int familyNameAtBirthMatchScore;
    private String addressMatch;
    private String streetNameMatch;
    private String streetNumberMatch;
    private String postalCodeMatch;
    private String regionMatch;
    private String localityMatch;
    private int localityMatchScore;
    private String countryMatch;
    private String houseNumberExtensionMatch;
    private String birthdateMatch;
    private String emailMatch;
    private String genderMatch;
}

