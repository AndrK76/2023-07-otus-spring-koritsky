package ru.otus.andrk.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class LimitCountConfig {
    @Value("${test-system.max-count-questions-per-test}")
    private int maxCountQuestionsPerTest;
}
