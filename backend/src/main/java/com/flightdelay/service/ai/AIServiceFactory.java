package com.flightdelay.service.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AIServiceFactory {

    @Value("${ai.provider:gemini}")
    private String provider;

    private final AIProviderService geminiService;
    private final AIProviderService claudeService;

    public AIServiceFactory(
            @Qualifier("gemini") AIProviderService geminiService,
            @Qualifier("claude") AIProviderService claudeService) {
        this.geminiService = geminiService;
        this.claudeService = claudeService;
    }

    public AIProviderService getService() {
        AIProviderService service = "claude".equalsIgnoreCase(provider) ? claudeService : geminiService;
        log.info("Using AI provider: {}", service.getProviderName());
        return service;
    }
}
