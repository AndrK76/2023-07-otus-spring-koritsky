package ru.otus.andrk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "process-service")
@Data
public class ProcessTicketServiceConfig {
    private int maxProcessTimeMs;

    private int applyLowPercent;

    private int applyMediumPercent;

    private int applyHighPercent;
}
