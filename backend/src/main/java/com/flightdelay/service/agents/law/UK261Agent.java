package com.flightdelay.service.agents.law;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface UK261Agent {

    @SystemMessage("""
            You are a specialist in UK Retained Regulation 261/2004 (UK261) only.

            APPLICABILITY:
            ✅ ANY flight departing a UK airport (any airline)
            ✅ Flight TO UK operated by UK carrier
            ❌ Flight TO UK by non-UK carrier does NOT qualify
            ❌ Does NOT cover EU-to-EU flights post-Brexit

            COMPENSATION (in GBP):
            <1500km delay 3hr+ → £220
            1500-3500km delay 3hr+ → £350
            >3500km delay 3hr+ → £520

            CARE RIGHTS:
            2hr+ → meals, refreshments, 2 communications
            Overnight → hotel + transport to/from airport

            ENFORCEMENT:
            - Civil Aviation Authority (CAA) — primary regulator
            - Alternative Dispute Resolution (ADR) schemes
            - Small Claims Court (up to £10,000)
            - 6-year claim window (England and Wales)
            - 5-year claim window (Scotland)

            KEY RULES:
            - Extraordinary circumstances mirror EU261 definitions
            - Airlines must provide written notice of rights
            - Burden of proof for extraordinary on airline

            Respond in valid JSON only.
            """)
    @UserMessage("""
            Evaluate UK261 for:
            Flight: {{flightNumber}}
            Route: {{departureAirport}} ({{departureCountry}})
                   → {{arrivalAirport}} ({{arrivalCountry}})
            Airline: {{airlineIata}} UK carrier: {{isUkCarrier}}
            Distance: {{distanceKm}}km
            Delay: {{delayMinutes}} minutes
            Reason classification: {{reasonClassification}}
            Incident: {{incidentType}}

            Return JSON:
            {
              "applicable": true/false,
              "applicabilityReason": "why applies or not",
              "compensationAmount": "£220|£350|£520|£0",
              "compensationNumber": 0/220/350/520,
              "currency": "GBP",
              "careRights": ["list of immediate rights"],
              "claimDeadline": "6 years England/Wales, 5 years Scotland",
              "filingAuthority": "CAA or ADR scheme",
              "legalArticles": ["UK261 Article 7"],
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
            @V("isUkCarrier") boolean isUkCarrier,
            @V("distanceKm") int distanceKm,
            @V("delayMinutes") int delayMinutes,
            @V("reasonClassification") String reasonClassification,
            @V("incidentType") String incidentType
    );
}
