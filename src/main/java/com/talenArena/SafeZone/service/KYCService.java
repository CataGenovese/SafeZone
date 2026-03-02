package com.talenArena.SafeZone.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talenArena.SafeZone.dto.PersonInfoRequest;
import com.talenArena.SafeZone.dto.PersonInfoResponse;
import com.talenArena.SafeZone.dto.KycMatchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KYCService {
    @Value("${network.as.code.api.url}")
    private String apiUrl;

    @Value("${network.as.code.rapidapi.host}")
    private String rapidApiHost;

    @Value("${network.as.code.rapidapi.key}")
    private String rapidApiKey;

    @Value("${network.as.code.mock.enabled:false}")
    private boolean mockEnabled;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PersonInfoResponse getPersonInfo(PersonInfoRequest request) {
        if (mockEnabled) {
            PersonInfoResponse response = new PersonInfoResponse();
            response.setPhoneNumber(request.getPhoneNumber());
            response.setIdDocument("66666666q");
            response.setName("Federica Sanchez Arjona");
            response.setGivenName("Federica");
            response.setFamilyName("Sanchez Arjona1");
            response.setNameKanaHankaku("federica");
            response.setNameKanaZenkaku("Ｆｅｄｅｒｉｃａ");
            response.setMiddleNames("Sanchez");
            response.setFamilyNameAtBirth("YYYY");
            response.setAddress("Tokyo-to Chiyoda-ku Iidabashi 3-10");
            response.setStreetName("Nicolas S");
            response.setStreetNumber("4");
            response.setPostalCode("1028460");
            response.setRegion("Tokyo");
            response.setLocality("ZZZZ");
            response.setCountry("JP");
            response.setHouseNumberExtension("36");
            response.setBirthdate("1978-08-22");
            response.setEmail("abc@example.com");
            response.setGender("MALE");
            response.setCityOfBirth("Budapest");
            response.setCountryOfBirth("HU");
            response.setNationality("HU");
            return response;
        }
        // Aquí iría la llamada real a la API externa si no es mock
        return null;
    }

    public KycMatchResponse kycMatch(PersonInfoResponse request) {
        if (mockEnabled) {
            KycMatchResponse response = new KycMatchResponse();
            response.setIdDocumentMatch("true");
            response.setNameMatch("false");
            response.setGivenNameMatch("false");
            response.setFamilyNameMatch("false");
            response.setNameKanaHankakuMatch("not_available");
            response.setNameKanaZenkakuMatch("not_available");
            response.setMiddleNamesMatch("not_available");
            response.setFamilyNameAtBirthMatch("false");
            response.setFamilyNameAtBirthMatchScore(0);
            response.setAddressMatch("false");
            response.setStreetNameMatch("false");
            response.setStreetNumberMatch("false");
            response.setPostalCodeMatch("false");
            response.setRegionMatch("false");
            response.setLocalityMatch("false");
            response.setLocalityMatchScore(0);
            response.setCountryMatch("false");
            response.setHouseNumberExtensionMatch("not_available");
            response.setBirthdateMatch("false");
            response.setEmailMatch("false");
            response.setGenderMatch("not_available");
            return response;
        }
        // Aquí iría la llamada real a la API externa si no es mock
        return null;
    }
}
