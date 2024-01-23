package ru.otus.andrk.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.andrk.service.health.HealthService;

@Component
@RequiredArgsConstructor
public class CustomHealthIndicator implements HealthIndicator {
    private final HealthService healthService;


    @Override
    public Health health() {
        return healthService.getServerHealth();
    }
}
