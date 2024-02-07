package ru.otus.andrk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "app.backend", ignoreInvalidFields = true)
public class BackendConfig {
    private String appName;
}
