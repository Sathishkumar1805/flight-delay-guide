package com.flightdelay.service.ai;

import com.flightdelay.model.request.FlightIncidentRequest;
import com.flightdelay.model.response.CompensationResult;
import com.flightdelay.model.response.FlightVerification;

public interface AIProviderService {

    CompensationResult analyzeFlightIncident(FlightIncidentRequest request, FlightVerification verification);

    String getProviderName();
}
