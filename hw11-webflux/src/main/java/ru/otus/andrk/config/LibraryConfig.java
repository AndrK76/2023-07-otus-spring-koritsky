package ru.otus.andrk.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class LibraryConfig {
    private final long listDelayInMs;

    private final String defaultLang;

    public LibraryConfig(
            @Value("${book-app.list-delay-ms}") long listDelayInMs,
            @Value("${book-app.default-lang}")String defaultLang) {
        this.listDelayInMs = listDelayInMs;
        this.defaultLang = defaultLang;
    }
}
