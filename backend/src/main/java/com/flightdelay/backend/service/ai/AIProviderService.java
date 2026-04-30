package com.flightdelay.backend.service.ai;

import com.flightdelay.backend.model.request.FlightIncidentRequest;
import com.flightdelay.backend.model.response.CompensationResult;
import com.flightdelay.backend.model.response.FlightVerification;

public interface AIProviderService {

    CompensationResult analyzeFlightIncident(FlightIncidentRequest request, FlightVerification verification);

    String getProviderName();
}
