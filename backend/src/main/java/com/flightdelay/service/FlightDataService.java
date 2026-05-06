package com.flightdelay.service;

import com.flightdelay.model.request.FlightIncidentRequest;
import com.flightdelay.model.response.FlightVerification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightDataService {

    private final AviationStackService aviationStackService;
    private final OpenSkyService openSkyService;

    public FlightVerification getFlightVerification(FlightIncidentRequest request) {
        try {
            var result = aviationStackService.getFlightData(request.getFlightNumber(), request.getFlightDate());
            if (result.isPresent()) {
                log.info("Flight data from AviationStack for {}", request.getFlightNumber());
                return result.get();
            }

            result = openSkyService.getFlightData(request.getFlightNumber(), request.getFlightDate());
            if (result.isPresent()) {
                log.info("Flight data from OpenSky for {}", request.getFlightNumber());
                return result.get();
            }
        } catch (Exception e) {
            log.warn("Flight data lookup failed, using user-provided data: {}", e.getMessage());
        }

        log.info("Using user-provided data for {}", request.getFlightNumber());
        return FlightVerification.builder()
                .flightNumber(request.getFlightNumber())
                .airline(request.getAirline())
                .delayMinutes(request.getDelayDuration() != null ? request.getDelayDuration() * 60 : null)
                .status(request.getIncidentType().name())
                .source(FlightVerification.Source.USER_PROVIDED)
                .build();
    }
}
