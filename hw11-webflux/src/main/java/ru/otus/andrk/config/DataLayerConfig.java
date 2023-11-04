package ru.otus.andrk.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Scheduler;

@Component
@Getter
public class DataLayerConfig {
    private final long waitDataInMs;

    private final Scheduler scheduler;

    public DataLayerConfig(
            @Value("${book-app.wait-data-ms:500}") long waitDataInMs,
            Scheduler scheduler) {
        this.waitDataInMs = waitDataInMs;
        this.scheduler = scheduler;
    }
}
