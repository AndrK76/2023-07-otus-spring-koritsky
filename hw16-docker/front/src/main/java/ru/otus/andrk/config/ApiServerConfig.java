package ru.otus.andrk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "app.api-server", ignoreInvalidFields = true)
public class ApiServerConfig {
    private String url;
}
