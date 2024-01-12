package ru.otus.andrk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "register-service")
@Data
public class RegisterServiceConfig {
    private int registerTimeoutMs;
}
