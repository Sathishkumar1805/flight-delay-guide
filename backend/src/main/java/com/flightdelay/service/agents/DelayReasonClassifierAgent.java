package com.flightdelay.service.agents;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

/**
 * AGENTIC PATTERN: Tool Use
 * Agent autonomously decides WHEN to call tools.
 * Without tools: "Airline says weather → extraordinary (guess)"
 * With tools: "Airline says weather → check API → clear skies
 *              → NOT extraordinary → passenger gets €400"
 * Tools registered in AgenticConfig.java (Phase 5).
 */
@AiService
public interface DelayReasonClassifierAgent {

    @SystemMessage("""
            You are an aviation law specialist in "extraordinary circumstances".

            ORDINARY (airline LIABLE — passenger gets compensation):
            - Technical/mechanical failures
            - Crew availability issues
            - Late aircraft from previous rotation
            - Fueling or cleaning delays
            - IT system failures
            - "Operational reasons" (vague — burden of proof on airline)

            EXTRAORDINARY (airline NOT liable):
            - Severe weather making operations unsafe
            - ATC strikes (not airline strikes)
            - Security threats, political instability
            - Bird strikes causing significant damage
            - Hidden manufacturing defects

            KEY LEGAL RULE: Airlines must PROVE extraordinary.
            Vague reasons default to ORDINARY.
            ECJ ruling: Technical problems are NOT extraordinary
            unless caused by undetectable hidden defect.

            You have tools available — use them when you need to verify:
            - checkWeatherConditions: verify if weather was actually severe
            - searchAirlineCompensationPolicy: check airline's claim history
            - checkAirlineNews: find known issues on that date

            Think step by step. Respond in valid JSON only.
            """)
    @UserMessage("""
            Classify delay reason for:
            FLIGHT: {{flightNumber}} on {{flightDate}}
            DEPARTURE: {{departureAirport}}
            AIRLINE REASON: "{{airlineReason}}"
            DELAY: {{delayMinutes}} minutes
            VERIFICATION CONTEXT: {{verificationContext}}

            STEP 1: What does the stated reason suggest?
            STEP 2: Does it meet the LEGAL definition of extraordinary?
            STEP 3: Should I verify with available tools?
            STEP 4: Final classification with confidence.

            Return JSON:
            {
              "classification": "EXTRAORDINARY|ORDINARY|AMBIGUOUS",
              "legalBasis": "specific legal provision applied",
              "reasoning": "your step-by-step analysis",
              "airlineCanEvade": true/false,
              "evidenceRecommendation": "what passenger should gather",
              "confidence": 0.0-1.0,
              "flagForReview": true/false,
              "reviewReason": "why flagged if applicable"
            }
            """)
    String classifyDelayReason(
            @V("flightNumber") String flightNumber,
            @V("airlineReason") String airlineReason,
            @V("delayMinutes") int delayMinutes,
            @V("flightDate") String flightDate,
            @V("departureAirport") String departureAirport,
            @V("verificationContext") String verificationContext
    );
}
