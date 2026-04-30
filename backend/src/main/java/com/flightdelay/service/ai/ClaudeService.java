package com.flightdelay.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightdelay.model.request.FlightIncidentRequest;
import com.flightdelay.model.response.CompensationResult;
import com.flightdelay.model.response.FlightVerification;
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
@Service("claude")
@RequiredArgsConstructor
public class ClaudeService implements AIProviderService {

    @Value("${ai.claude.api-key}")
    private String apiKey;

    @Value("${ai.claude.model}")
    private String model;

    @Value("${ai.claude.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final PromptBuilderService promptBuilder;
    private final ObjectMapper objectMapper;

    @Override
    public CompensationResult analyzeFlightIncident(FlightIncidentRequest request, FlightVerification verification) {
        try {
            String prompt = promptBuilder.buildCompensationPrompt(request, verification);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", apiKey);
            headers.set("anthropic-version", "2023-06-01");

            Map<String, Object> body = Map.of(
                    "model", model,
                    "max_tokens", 2000,
                    "messages", List.of(Map.of("role", "user", "content", prompt))
            );

            ResponseEntity<String> response = restTemplate.postForEntity(
                    baseUrl + "/messages", new HttpEntity<>(body, headers), String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            String text = root.at("/content/0/text").asText();

            return objectMapper.readValue(extractJson(text), CompensationResult.class);
        } catch (Exception e) {
            log.error("Claude API error: {}", e.getMessage());
            return CompensationResult.builder()
                    .eligible(false)
                    .errorMessage("AI analysis unavailable: " + e.getMessage())
                    .confidence(CompensationResult.Confidence.LOW)
                    .build();
        }
    }

    @Override
    public String getProviderName() {
        return "claude";
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
