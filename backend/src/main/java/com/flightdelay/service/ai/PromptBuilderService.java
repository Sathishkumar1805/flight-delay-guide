package com.flightdelay.service.ai;

import com.flightdelay.model.request.FlightIncidentRequest;
import com.flightdelay.model.response.FlightVerification;
import org.springframework.stereotype.Service;

@Service
public class PromptBuilderService {

    public String buildCompensationPrompt(FlightIncidentRequest request, FlightVerification verification) {
        return """
                You are an expert in airline passenger rights and compensation regulations (EC 261/2004 and equivalents).
                Analyze the flight incident below and determine compensation eligibility.

                FLIGHT INCIDENT:
                - Flight: %s operated by %s (%s)
                - Date: %s
                - Route: %s (%s) → %s (%s)
                - Incident type: %s
                - Reported delay: %s hours
                - Airline reason: %s

                VERIFIED FLIGHT DATA:
                - Source: %s
                - Actual delay: %s minutes
                - Status: %s

                Respond ONLY with a valid JSON object in this exact format:
                {
                  "eligible": true or false,
                  "regulation": "EC 261/2004" or applicable regulation name,
                  "compensationAmount": "250" or "400" or "600" or "0",
                  "compensationCurrency": "EUR",
                  "delayMinutes": number,
                  "confidence": "HIGH" or "MEDIUM" or "LOW",
                  "immediateRights": ["right 1", "right 2", "right 3"],
                  "explanation": "clear explanation of the eligibility determination",
                  "demandLetter": "formal letter text the passenger can send to the airline"
                }

                EC 261/2004 compensation thresholds (3+ hour delays, non-extraordinary):
                - €250 for flights up to 1500 km
                - €400 for intra-EU flights over 1500 km and other flights 1500-3500 km
                - €600 for flights over 3500 km
                Consider extraordinary circumstances (weather, ATC strikes, security) which exempt airlines.
                """.formatted(
                request.getFlightNumber(),
                request.getAirlineName(),
                request.getAirlineIata(),
                request.getFlightDate(),
                request.getDepartureAirport(), request.getDepartureCountry(),
                request.getArrivalAirport(), request.getArrivalCountry(),
                request.getIncidentType(),
                request.getDelayHours() != null ? request.getDelayHours() : "unknown",
                request.getAirlineReason() != null ? request.getAirlineReason() : "not provided",
                verification.getSource(),
                verification.getDelayMinutes() != null ? verification.getDelayMinutes() : "unknown",
                verification.getStatus() != null ? verification.getStatus() : "unknown"
        );
    }
}
