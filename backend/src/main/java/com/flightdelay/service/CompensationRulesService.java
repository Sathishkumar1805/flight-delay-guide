package com.flightdelay.service;

import com.flightdelay.model.request.FlightIncidentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;

@Slf4j
@Service
public class CompensationRulesService {

    private static final Set<String> EU_COUNTRY_CODES = Set.of(
            "AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR",
            "DE", "GR", "HU", "IE", "IT", "LV", "LT", "LU", "MT", "NL",
            "PL", "PT", "RO", "SK", "SI", "ES", "SE",
            "IS", "LI", "NO", "CH"
    );

    private static final Set<String> EU_AIRLINE_IATA_CODES = Set.of(
            "LH", "FR", "U2", "BA", "AF", "KL", "IB", "VY", "W6", "TP",
            "SK", "AY", "LX", "OS", "SN", "EI", "DY", "LO", "OK", "A3"
    );

    private static final Set<String> EXTRAORDINARY_KEYWORDS = Set.of(
            "weather", "storm", "snow", "fog", "wind", "ice",
            "atc", "air traffic", "strike", "security", "political",
            "medical", "bird strike", "bird-strike", "technical"
    );

    public String determineRegulation(FlightIncidentRequest request) {
        boolean depEu = EU_COUNTRY_CODES.contains(request.getDepartureCountry().toUpperCase(Locale.ROOT));
        boolean arrEu = EU_COUNTRY_CODES.contains(request.getArrivalCountry().toUpperCase(Locale.ROOT));
        boolean euAirline = EU_AIRLINE_IATA_CODES.contains(request.getAirlineIata().toUpperCase(Locale.ROOT));

        if (depEu || (arrEu && euAirline)) {
            return "EC 261/2004";
        }
        if ("GB".equalsIgnoreCase(request.getDepartureCountry())
                || ("GB".equalsIgnoreCase(request.getArrivalCountry()) && euAirline)) {
            return "UK261";
        }
        return "LOCAL_REGULATIONS";
    }

    public boolean isExtraordinaryCircumstance(String airlineReason) {
        if (airlineReason == null || airlineReason.isBlank()) {
            return false;
        }
        String lower = airlineReason.toLowerCase(Locale.ROOT);
        return EXTRAORDINARY_KEYWORDS.stream().anyMatch(lower::contains);
    }
}
