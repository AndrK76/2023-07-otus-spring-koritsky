package ru.otus.andrk.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class LibraryConfig {
    private final long listDelayInMs;

    public LibraryConfig(
            @Value("${book-app.list-delay-ms}") long listDelayInMs) {
        this.listDelayInMs = listDelayInMs;
    }
}
