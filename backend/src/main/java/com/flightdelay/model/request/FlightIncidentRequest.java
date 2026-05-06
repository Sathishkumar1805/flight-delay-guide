package com.flightdelay.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class FlightIncidentRequest {

    @NotBlank
    private String flightNumber;

    @NotBlank
    private String airline;

    @NotNull
    private LocalDate flightDate;

    @NotBlank
    private String departureAirport;

    @NotBlank
    private String arrivalAirport;

    @NotBlank
    private String departureCountry;

    @NotBlank
    private String arrivalCountry;

    @NotNull
    private IncidentType incidentType;

    private Integer delayDuration;

    private String airlineReason;

    public enum IncidentType {
        DELAY, CANCELLATION, DENIED_BOARDING
    }
}
