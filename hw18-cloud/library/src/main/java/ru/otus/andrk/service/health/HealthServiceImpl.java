package ru.otus.andrk.service.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HealthServiceImpl implements HealthService {

    private static final long MAX_UNVISITED_INTERVAL_MS = 15_000L;

    private Date lastVisit = new Date();

    @Override
    public void registerVisitor() {
        synchronized (this) {
            lastVisit = new Date();
        }
    }

    @Override
    public Health getServerHealth() {
        synchronized (this) {
            var timeDiff = (new Date()).getTime() - lastVisit.getTime();
            if (timeDiff < MAX_UNVISITED_INTERVAL_MS) {
                return Health.up().withDetail("message", "Нас помнят").build();
            } else {
                return Health.down().withDetail("message", "О нас забыли").build();
            }
        }
    }
}
