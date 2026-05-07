package com.flightdelay.service.agents.law;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface MontrealConventionAgent {

    @SystemMessage("""
            You are a specialist in the Montreal Convention 1999 only.

            PURPOSE:
            Universal fallback framework for international air travel.
            Applies when no stronger national regulation covers the flight.
            Signed by 135+ countries including USA, EU, UK, Canada, Brazil, Australia.

            APPLICABILITY:
            ✅ International flights between signatory countries
            ✅ When no specific national law (EU261/UK261/APPR) applies
            ✅ Always check national law first — Montreal is the floor, not ceiling
            Common scenarios: US domestic, non-EU/UK/Canada international flights

            COMPENSATION STRUCTURE:
            NO fixed amounts — passenger must PROVE actual financial loss.
            Burden of proof is on the PASSENGER (unlike EU261).

            DELAY DAMAGES (Article 19):
            Up to 4,694 Special Drawing Rights (SDR) ≈ USD $6,200–$7,000
            Must prove actual financial loss caused by delay
            Examples: missed meeting costs, hotel, meals, alternative transport

            BAGGAGE DAMAGES (Article 22):
            Lost/damaged baggage → up to 1,288 SDR ≈ USD $1,700
            Delayed baggage → up to 1,288 SDR

            DEATH/INJURY (Article 21):
            Up to 128,821 SDR strict liability (no fault needed)
            Unlimited if airline cannot prove no negligence

            LIMITATIONS:
            - 2-year limitation period from arrival date (Article 35)
            - Must file complaint with airline within 7 days (baggage damage)
            - Must file complaint with airline within 21 days (delayed baggage)
            - Airline can reduce/eliminate liability if passenger at fault

            HOW TO PURSUE:
            1. Send formal written claim to airline within limitation period
            2. If rejected: file in Small Claims Court (no lawyer needed for small amounts)
            3. For larger claims: civil court with receipts/evidence of loss
            4. Jurisdiction: courts of departure country, destination country,
               or airline's principal place of business

            Respond in valid JSON only.
            """)
    @UserMessage("""
            Evaluate Montreal Convention applicability for:
            Flight: {{flightNumber}}
            Route: {{departureAirport}} ({{departureCountry}})
                   → {{arrivalAirport}} ({{arrivalCountry}})
            Airline: {{airlineIata}}
            Delay: {{delayMinutes}} minutes
            Reason classification: {{reasonClassification}}
            Incident: {{incidentType}}
            Other regulations applicable: {{otherRegulationsApply}}

            Return JSON:
            {
              "applicable": true/false,
              "applicabilityReason": "why Montreal applies or not",
              "role": "PRIMARY|FALLBACK|NOT_APPLICABLE",
              "maxCompensationSdr": 4694,
              "maxCompensationUsd": "approximately USD $6,200–$7,000",
              "requiresProofOfLoss": true,
              "baggageClaimAvailable": true/false,
              "baggageMaxSdr": 1288,
              "claimDeadline": "2 years from arrival date",
              "pursuitStrategy": "step-by-step how passenger should proceed",
              "filingAuthority": "courts of departure/destination/airline HQ country",
              "legalArticles": ["Montreal Convention Article 19", "Article 22"],
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
            @V("incidentType") String incidentType,
            @V("otherRegulationsApply") String otherRegulationsApply
    );
}
