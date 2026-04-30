package com.flightdelay.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightVerification {

    private String flightNumber;
    private String airline;
    private ZonedDateTime departureScheduled;
    private ZonedDateTime departureActual;
    private ZonedDateTime arrivalScheduled;
    private ZonedDateTime arrivalActual;
    private Integer delayMinutes;
    private String status;
    private Source source;

    public enum Source {
        AVIATIONSTACK, OPENSKY, USER_PROVIDED
    }
}
