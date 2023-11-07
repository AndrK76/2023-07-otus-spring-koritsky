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

    private final boolean useErrorSource;

    private final int errorMessageIndex;

    public ControllerConfig(
            @Value("${book-app.list-delay-ms:0}") long listDelayInMs,
            Scheduler scheduler,
            @Value("${book-app.use-error-source:false}") boolean useErrorSource,
            @Value("${book-app.error-message-index:1}") int errorMessageIndex
            ) {
        this.listDelayInMs = listDelayInMs;
        this.scheduler = scheduler;
        this.useErrorSource = useErrorSource;
        this.errorMessageIndex = errorMessageIndex;
    }
}
