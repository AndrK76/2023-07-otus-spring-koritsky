package ru.otus.andrk.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsDaoCsvConfig {
    @Value("${questions.csv-delimiter}")
    private String csvDelimiter;

    @Value("${questions.resource-file}")
    private String resourceName;
}
