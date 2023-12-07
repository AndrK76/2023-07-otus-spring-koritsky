package ru.otus.andrk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "app.i18n", ignoreInvalidFields = true)
public class I18nConfig {
    private String defaultLang;

    private String messageBundle;
}
