package ru.otus.andrk.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.cors", ignoreInvalidFields = true)
public class CorsConfig {
    private List<String> origins;
}
