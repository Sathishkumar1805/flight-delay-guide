package com.flightdelay.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightdelay.backend.model.response.FlightVerification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AviationStackService {

    @Value("${aviation.aviationstack.api-key}")
    private String apiKey;

    @Value("${aviation.aviationstack.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Cacheable("flightData")
    public Optional<FlightVerification> getFlightData(String flightNumber, LocalDate flightDate) {
        try {
            if ("REPLACE_WITH_REAL_KEY".equals(apiKey)) {
                log.warn("AviationStack API key not configured — skipping");
                return Optional.empty();
            }

            String url = "%s/flights?access_key=%s&flight_iata=%s&flight_date=%s"
                    .formatted(baseUrl, apiKey, flightNumber, flightDate);

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode data = root.path("data");

            if (!data.isArray() || data.isEmpty()) {
                return Optional.empty();
            }

            JsonNode flight = data.get(0);
            int delayMinutes = flight.path("departure").path("delay").asInt(0);

            return Optional.of(FlightVerification.builder()
                    .flightNumber(flightNumber)
                    .airline(flight.path("airline").path("name").asText())
                    .status(flight.path("flight_status").asText())
                    .delayMinutes(delayMinutes)
                    .source(FlightVerification.Source.AVIATIONSTACK)
                    .build());

        } catch (RestClientException e) {
            log.warn("AviationStack request failed: {}", e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.error("AviationStack parsing error: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
