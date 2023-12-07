package ru.otus.andrk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "app.keycloak", ignoreInvalidFields = true)
public class KeyCloakConfig {
    private String url;

    private String realm;

    private String client;
}
