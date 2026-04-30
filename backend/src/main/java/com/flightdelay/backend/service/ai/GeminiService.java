package com.flightdelay.backend.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightdelay.backend.model.request.FlightIncidentRequest;
import com.flightdelay.backend.model.response.CompensationResult;
import com.flightdelay.backend.model.response.FlightVerification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service("gemini")
@RequiredArgsConstructor
public class GeminiService implements AIProviderService {

    @Value("${ai.gemini.api-key}")
    private String apiKey;

    @Value("${ai.gemini.model}")
    private String model;

    @Value("${ai.gemini.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final PromptBuilderService promptBuilder;
    private final ObjectMapper objectMapper;

    @Override
    public CompensationResult analyzeFlightIncident(FlightIncidentRequest request, FlightVerification verification) {
        try {
            String prompt = promptBuilder.buildCompensationPrompt(request, verification);
            String url = "%s/models/%s:generateContent?key=%s".formatted(baseUrl, model, apiKey);

            Map<String, Object> body = Map.of(
                    "contents", List.of(Map.of(
                            "parts", List.of(Map.of("text", prompt))
                    ))
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    url, new HttpEntity<>(body, headers), String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            String text = root.at("/candidates/0/content/parts/0/text").asText();

            return objectMapper.readValue(extractJson(text), CompensationResult.class);
        } catch (Exception e) {
            log.error("Gemini API error: {}", e.getMessage());
            return CompensationResult.builder()
                    .eligible(false)
                    .errorMessage("AI analysis unavailable: " + e.getMessage())
                    .confidence(CompensationResult.Confidence.LOW)
                    .build();
        }
    }

    @Override
    public String getProviderName() {
        return "gemini";
    }

    private String extractJson(String text) {
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        return text;
    }
}
