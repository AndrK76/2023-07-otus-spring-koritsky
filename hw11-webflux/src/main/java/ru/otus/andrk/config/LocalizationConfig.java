package ru.otus.andrk.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Scheduler;

@Component
@Getter
public class LocalizationConfig {
    private final String defaultLang;

    private final String messageBundle;

    private final Scheduler scheduler;

    public LocalizationConfig(
            @Value("${book-app.default-lang}") String defaultLang,
            @Value("${spring.messages.basename:messages}") String messageBundle,
            Scheduler scheduler) {
        this.defaultLang = defaultLang;
        this.messageBundle = messageBundle;
        this.scheduler = scheduler;
    }
}
