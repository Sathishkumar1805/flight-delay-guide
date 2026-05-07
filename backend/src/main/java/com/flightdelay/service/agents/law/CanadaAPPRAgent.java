package com.flightdelay.service.agents.law;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface CanadaAPPRAgent {

    @SystemMessage("""
            You are a specialist in Canada's Air Passenger Protection Regulations (APPR) only.

            APPLICABILITY:
            ✅ All flights departing from Canada
            ✅ All flights arriving in Canada
            ✅ All flights within Canada
            Applies to all airlines operating in Canada regardless of origin.

            COMPENSATION — LARGE CARRIERS (Air Canada, WestJet, etc.):
            Delay 3–6 hours     → CAD $400
            Delay 6–9 hours     → CAD $700
            Delay 9+ hours      → CAD $1,000
            Denied boarding     → CAD $900 (under 2hr delay to dest.)
                                → CAD $1,800 (2–6hr delay)
                                → CAD $2,400 (6hr+ delay)

            COMPENSATION — SMALL CARRIERS:
            Delay 3–6 hours     → CAD $125
            Delay 6–9 hours     → CAD $250
            Delay 9+ hours      → CAD $500

            CARE RIGHTS:
            Delay 2hr+ → food/drink vouchers, free communication
            Delay 3hr+ → rebooking or refund
            Overnight → hotel accommodation + transport

            ENFORCEMENT:
            - Canadian Transportation Agency (CTA)
            - File complaint within 1 year of incident
            - Airline must respond within 30 days
            - CTA adjudication if unresolved

            EXEMPTIONS:
            - Safety reasons (must be documented)
            - Situations outside airline control
            - But: airline must PROVE the exemption applies

            Respond in valid JSON only.
            """)
    @UserMessage("""
            Evaluate Canada APPR for:
            Flight: {{flightNumber}}
            Route: {{departureAirport}} ({{departureCountry}})
                   → {{arrivalAirport}} ({{arrivalCountry}})
            Airline: {{airlineIata}} Large carrier: {{isLargeCarrier}}
            Delay: {{delayMinutes}} minutes
            Reason classification: {{reasonClassification}}
            Incident: {{incidentType}}

            Return JSON:
            {
              "applicable": true/false,
              "applicabilityReason": "why applies or not",
              "carrierSize": "LARGE|SMALL",
              "compensationAmount": "CAD $400|$700|$1000|$0",
              "compensationNumber": 0/125/250/400/500/700/1000,
              "currency": "CAD",
              "careRights": ["list of immediate rights"],
              "claimDeadline": "1 year from incident",
              "filingAuthority": "Canadian Transportation Agency (CTA)",
              "legalArticles": ["APPR Section 19", "APPR Section 12"],
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
            @V("isLargeCarrier") boolean isLargeCarrier,
            @V("delayMinutes") int delayMinutes,
            @V("reasonClassification") String reasonClassification,
            @V("incidentType") String incidentType
    );
}
