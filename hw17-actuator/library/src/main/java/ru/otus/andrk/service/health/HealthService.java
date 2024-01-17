package ru.otus.andrk.service.health;

import org.springframework.boot.actuate.health.Health;

public interface HealthService {
    void registerVisitor();

    Health getServerHealth();

}
