package com.flightdelay.backend.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompensationResult {

    private boolean eligible;
    private String regulation;
    private String compensationAmount;
    private String compensationCurrency;
    private Integer delayMinutes;
    private boolean verifiedByApi;
    private Confidence confidence;
    private List<String> immediateRights;
    private String explanation;
    private String demandLetter;
    private String errorMessage;
    private FlightVerification flightVerification;

    public enum Confidence {
        HIGH, MEDIUM, LOW
    }
}
