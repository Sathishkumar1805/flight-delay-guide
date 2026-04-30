package com.flightdelay.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupValidator implements ApplicationRunner {

    @Value("${ai.gemini.api-key}")
    private String geminiKey;

    @Value("${aviation.aviationstack.api-key}")
    private String aviationKey;

    @Override
    public void run(ApplicationArguments args) {
        if ("REPLACE_WITH_REAL_KEY".equals(geminiKey)) {
            log.warn("⚠️  GEMINI_API_KEY not set — AI features will fail");
        }
        if ("REPLACE_WITH_REAL_KEY".equals(aviationKey)) {
            log.warn("⚠️  AVIATIONSTACK_API_KEY not set — flight verification disabled");
        }
    }
}
