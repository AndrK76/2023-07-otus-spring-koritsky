package ru.otus.andrk.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Scheduler;

@Component
@Getter
public class ControllerConfig {
    private final long listDelayInMs;

    private final Scheduler scheduler;

    public ControllerConfig(
            @Value("${book-app.list-delay-ms:0}") long listDelayInMs,
            Scheduler scheduler) {
        this.listDelayInMs = listDelayInMs;
        this.scheduler = scheduler;
    }
}
