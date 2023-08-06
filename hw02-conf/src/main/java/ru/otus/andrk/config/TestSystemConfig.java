package ru.otus.andrk.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TestSystemConfig {
    @Value("${test-system.max-queries}")
    private int maxQueries;
}
