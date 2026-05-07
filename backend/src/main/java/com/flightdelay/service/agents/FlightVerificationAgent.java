package com.flightdelay.service.agents;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

/**
 * AGENTIC PATTERN: Specialist Agent + Chain Workflow
 * ONE job only. Does NOT know about compensation laws.
 * Output flows to next agent in chain.
 * @AiService = LangChain4j generates implementation at runtime.
 * @V("name") = injects value into {{name}} placeholder in prompt.
 */
@AiService
public interface FlightVerificationAgent {

    @SystemMessage("""
            You are an expert flight data analyst.
            Analyze raw aviation API data and return enriched verification.

            Your job:
            1. Confirm actual delay minutes from API data
            2. Assess data reliability (HIGH/MEDIUM/LOW)
            3. Estimate flight distance from airport codes
            4. Flag any discrepancies between API data and user report
            5. Note delay timing patterns

            Respond ONLY in valid JSON. No markdown.
            """)
    @UserMessage("""
            FLIGHT: {{flightNumber}} on {{flightDate}}
            AIRLINE: {{airlineName}} ({{airlineIata}})
            ROUTE: {{departureAirport}} ({{departureCountry}})
                   → {{arrivalAirport}} ({{arrivalCountry}})
            RAW API DATA: {{rawFlightData}}
            USER REPORTED: {{incidentType}}, {{delayHours}} hours
            AIRLINE REASON: {{airlineReason}}

            Return JSON:
            {
              "flightConfirmed": true/false,
              "actualDelayMinutes": number,
              "dataSource": "AVIATIONSTACK|OPENSKY|USER_PROVIDED",
              "dataReliability": "HIGH|MEDIUM|LOW",
              "flightDistanceKm": number,
              "distanceCategory": "SHORT|MEDIUM|LONG",
              "userReportMatchesApi": true/false,
              "discrepancies": ["list any differences"],
              "delayPatternNotes": "observations",
              "confidence": 0.0-1.0
            }
            """)
    String verifyFlight(
            @V("flightNumber") String flightNumber,
            @V("flightDate") String flightDate,
            @V("airlineName") String airlineName,
            @V("airlineIata") String airlineIata,
            @V("departureAirport") String departureAirport,
            @V("departureCountry") String departureCountry,
            @V("arrivalAirport") String arrivalAirport,
            @V("arrivalCountry") String arrivalCountry,
            @V("rawFlightData") String rawFlightData,
            @V("incidentType") String incidentType,
            @V("delayHours") Integer delayHours,
            @V("airlineReason") String airlineReason
    );
}
