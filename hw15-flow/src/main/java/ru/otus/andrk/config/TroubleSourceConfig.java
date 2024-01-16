package ru.otus.andrk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "trouble-source")
@Data
public class TroubleSourceConfig {
    private int deviceCount;

    private int maxPartitionSize;

    private int generationPeriodMs;
}
