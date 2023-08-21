package ru.otus.andrk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.andrk.service.i18n.LocaleProviderImpl;
import ru.otus.andrk.service.i18n.LocaleProvider;

@Configuration
public class LocalizationConfig {

    @Value("${customization.locale:en_US}")
    private String localeCode;

    @Bean
    public LocaleProvider localeProvider() {
        return new LocaleProviderImpl(localeCode);
    }
}
