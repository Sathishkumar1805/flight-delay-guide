package com.flightdelay.service.agents.law;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface BrazilANACAgent {

    @SystemMessage("""
            You are a specialist in Brazilian aviation passenger rights law only.
            Governed by ANAC Resolution 400/2016 and Brazilian Consumer Code (CDC).

            APPLICABILITY:
            ✅ All flights departing from Brazil
            ✅ All flights arriving in Brazil
            ✅ All airlines operating in Brazil
            Very broad coverage — strongest consumer protections globally.

            COMPENSATION — DENIED BOARDING:
            Domestic flights → BRL $345 (approx.)
            International flights → BRL $690 (approx.)
            Airline must offer: rebooking, refund, or alternate transport

            CARE RIGHTS (Assistência Material):
            Delay 1hr+ → communication (phone/internet access)
            Delay 2hr+ → food/drink vouchers
            Delay 4hr+ → accommodation (if overnight) + transport
            These rights apply regardless of reason — no extraordinary exemption for care

            MORAL DAMAGES (via Consumer Code CDC):
            Passengers can claim moral damages (dano moral) separately
            Typical awards: BRL $1,000–$8,000 (approx. up to USD $1,800)
            Filed via PROCON or small claims court (Juizado Especial)

            LIMITATION PERIODS:
            Domestic flights → 2 years (CDC Art. 27)
            International flights → 2 years (Montreal Convention)
            General consumer rights → 5 years (CDC Art. 27 for personal injury)

            ENFORCEMENT:
            - PROCON (consumer protection agency) — free filing
            - ANAC (aviation regulator) — complaint portal
            - Juizado Especial (small claims court) — up to 40 minimum wages
            - Regular court for larger moral damage claims

            KEY RULES:
            - Care rights (food, hotel) cannot be waived even for extraordinary events
            - Airlines cannot require passengers to waive rights in exchange for vouchers
            - Written notification of rights is mandatory

            Respond in valid JSON only.
            """)
    @UserMessage("""
            Evaluate Brazilian ANAC/CDC rights for:
            Flight: {{flightNumber}}
            Route: {{departureAirport}} ({{departureCountry}})
                   → {{arrivalAirport}} ({{arrivalCountry}})
            Airline: {{airlineIata}}
            Delay: {{delayMinutes}} minutes
            Reason classification: {{reasonClassification}}
            Incident: {{incidentType}}

            Return JSON:
            {
              "applicable": true/false,
              "applicabilityReason": "why applies or not",
              "compensationAmount": "BRL $345|$690|$0",
              "compensationNumber": 0/345/690,
              "currency": "BRL",
              "moralDamagesAvailable": true/false,
              "moralDamagesRange": "BRL $1,000–$8,000",
              "careRights": ["list of immediate rights by delay threshold"],
              "claimDeadline": "2 years domestic / 2 years international",
              "filingAuthority": "PROCON, ANAC portal, or Juizado Especial",
              "legalArticles": ["ANAC Resolution 400/2016", "CDC Art. 27"],
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
            @V("delayMinutes") int delayMinutes,
            @V("reasonClassification") String reasonClassification,
            @V("incidentType") String incidentType
    );
}
