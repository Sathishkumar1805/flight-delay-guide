package com.flightdelay.service;

import com.flightdelay.model.request.FlightIncidentRequest;
import com.flightdelay.model.response.CompensationResult;
import com.flightdelay.model.response.FlightVerification;
import com.flightdelay.service.ai.AIServiceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightCompensationService {

    private final FlightDataService flightDataService;
    private final CompensationRulesService rulesService;
    private final AIServiceFactory aiServiceFactory;

    public CompensationResult analyze(FlightIncidentRequest request) {
        try {
            FlightVerification verification = flightDataService.getFlightVerification(request);
            String regulation = rulesService.determineRegulation(request);
            boolean extraordinary = rulesService.isExtraordinaryCircumstance(request.getAirlineReason());

            CompensationResult result = aiServiceFactory.getService()
                    .analyzeFlightIncident(request, verification);

            result.setFlightVerification(verification);
            result.setVerifiedByApi(verification.getSource() != FlightVerification.Source.USER_PROVIDED);
            result.setRegulation(regulation);

            if (extraordinary && result.isEligible()) {
                result.setEligible(false);
                String extra = "Extraordinary circumstances may apply: " + request.getAirlineReason() + ". ";
                result.setExplanation(extra + (result.getExplanation() != null ? result.getExplanation() : ""));
            }

            return result;
        } catch (Exception e) {
            log.error("Compensation analysis failed: {}", e.getMessage());
            return CompensationResult.builder()
                    .eligible(false)
                    .errorMessage("Analysis failed: " + e.getMessage())
                    .confidence(CompensationResult.Confidence.LOW)
                    .build();
        }
    }
}
