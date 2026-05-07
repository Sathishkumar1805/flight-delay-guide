package com.flightdelay.service.agents.law;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface EU261Agent {

    @SystemMessage("""
            You are a specialist in EU Regulation 261/2004 only.

            APPLICABILITY (Article 3):
            ✅ ANY flight departing EU airport (any airline)
            ✅ Flight TO EU operated by EU carrier
            ❌ Flight TO EU by non-EU carrier does NOT qualify

            COMPENSATION (Article 7):
            <1500km delay 3hr+ → €250
            1500-3500km delay 3hr+ → €400
            >3500km delay 3hr+ → €600

            CARE RIGHTS (Article 9):
            2hr+ → meals, refreshments, 2 phone calls
            Overnight → hotel + transport

            KEY ECJ PRECEDENTS:
            - Sturgeon: 3hr+ arrival delay = right to compensation
            - Wallentin-Hermann: Technical faults ≠ extraordinary
            - Connecting flights: final destination arrival delay counts

            Respond in valid JSON only.
            """)
    @UserMessage("""
            Evaluate EU261 for:
            Flight: {{flightNumber}}
            Route: {{departureAirport}} ({{departureCountry}})
                   → {{arrivalAirport}} ({{arrivalCountry}})
            Airline: {{airlineIata}} EU carrier: {{isEuCarrier}}
            Distance: {{distanceKm}}km
            Delay: {{delayMinutes}} minutes
            Reason classification: {{reasonClassification}}
            Incident: {{incidentType}}

            Return JSON:
            {
              "applicable": true/false,
              "applicabilityReason": "why applies or not",
              "compensationAmount": "€250|€400|€600|€0",
              "compensationNumber": 0/250/400/600,
              "currency": "EUR",
              "careRights": ["list of immediate rights"],
              "claimDeadline": "time limit to file",
              "filingAuthority": "which NEB to escalate to",
              "legalArticles": ["EU261 Article 7(1)(b)"],
              "confidence": 0.0-1.0
            }
            """)
    String evaluate(
            @V("flightNumber") String flightNumber,
            @V("departureAirport") String departureAirport,
            @V("departureCountry") String departureCountry,
            @V("arrivalAirport") String arrivalAirport,
            @V("arrivalCountry") String arrivalCountry,
            @V("airlineIata") String airlineIata,
            @V("isEuCarrier") boolean isEuCarrier,
            @V("distanceKm") int distanceKm,
            @V("delayMinutes") int delayMinutes,
            @V("reasonClassification") String reasonClassification,
            @V("incidentType") String incidentType
    );
}
