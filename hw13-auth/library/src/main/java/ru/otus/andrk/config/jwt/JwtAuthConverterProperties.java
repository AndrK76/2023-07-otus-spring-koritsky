package ru.otus.andrk.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@ConfigurationProperties(prefix = "jwt.auth.converter")
@Data
public class JwtAuthConverterProperties {
    private String resourceId;
    private String principalAttribute;
}
