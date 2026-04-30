package com.flightdelay.backend.controller;

import com.flightdelay.backend.model.request.FlightIncidentRequest;
import com.flightdelay.backend.model.response.CompensationResult;
import com.flightdelay.backend.model.response.FlightVerification;
import com.flightdelay.backend.service.FlightCompensationService;
import com.flightdelay.backend.service.FlightDataService;
import com.flightdelay.backend.service.ai.AIServiceFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/compensation")
@RequiredArgsConstructor
public class FlightCompensationController {

    private final FlightCompensationService compensationService;
    private final FlightDataService flightDataService;
    private final AIServiceFactory aiServiceFactory;

    @PostMapping("/analyze")
    public ResponseEntity<CompensationResult> analyzeIncident(@Valid @RequestBody FlightIncidentRequest request) {
        return ResponseEntity.ok(compensationService.analyze(request));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "aiProvider", aiServiceFactory.getService().getProviderName()
        ));
    }

    @GetMapping("/regulations")
    public ResponseEntity<List<Map<String, String>>> regulations() {
        return ResponseEntity.ok(List.of(
                Map.of("code", "EC 261/2004", "region", "EU",
                        "description", "Applies to flights departing EU airports or arriving at EU airports on EU carriers"),
                Map.of("code", "UK261", "region", "UK",
                        "description", "UK equivalent of EC 261/2004 post-Brexit"),
                Map.of("code", "LOCAL_REGULATIONS", "region", "Other",
                        "description", "Country-specific passenger rights regulations apply")
        ));
    }

    @PostMapping("/verify-flight")
    public ResponseEntity<FlightVerification> verifyFlight(@Valid @RequestBody FlightIncidentRequest request) {
        return ResponseEntity.ok(flightDataService.getFlightVerification(request));
    }
}
