package com.flightdelay.service.agentic;

import com.flightdelay.model.request.FlightIncidentRequest;
import com.flightdelay.model.response.CompensationResult;
import org.springframework.stereotype.Service;

@Service
public class AsyncAnalysisService {

    public String submitAnalysis(FlightIncidentRequest request) {
        // Phase 3: multi-agent orchestration flow
        throw new UnsupportedOperationException("Agentic flow not yet implemented — coming in Phase 3");
    }

    public CompensationResult waitForResult(String jobId, int timeoutSeconds) {
        // Phase 3: poll or block until agent pipeline completes
        throw new UnsupportedOperationException("Agentic flow not yet implemented — coming in Phase 3");
    }
}
