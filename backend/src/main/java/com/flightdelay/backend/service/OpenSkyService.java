package com.flightdelay.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightdelay.backend.model.response.FlightVerification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenSkyService {

    @Value("${aviation.opensky.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public Optional<FlightVerification> getFlightData(String flightNumber, LocalDate flightDate) {
        try {
            long epochStart = flightDate.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
            long epochEnd = flightDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toEpochSecond();

            String url = "%s/flights/all?begin=%d&end=%d".formatted(baseUrl, epochStart, epochEnd);
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            if (!root.isArray()) {
                return Optional.empty();
            }

            String searchNumber = flightNumber.toLowerCase();
            for (JsonNode flight : root) {
                String callsign = flight.path("callsign").asText("").trim().toLowerCase();
                if (callsign.contains(searchNumber)) {
                    return Optional.of(FlightVerification.builder()
                            .flightNumber(flightNumber)
                            .status("ACTIVE")
                            .source(FlightVerification.Source.OPENSKY)
                            .build());
                }
            }
            return Optional.empty();

        } catch (RestClientException e) {
            log.warn("OpenSky request failed: {}", e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("OpenSky parsing error: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
