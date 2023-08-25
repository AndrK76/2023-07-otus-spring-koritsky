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
    @Value("${test-system.questions.csv-delimiter}")
    private String csvDelimiter;

    @Value("${test-system.questions.resource-file-name}")
    private String resourceName;

    @Value("${test-system.questions.resource-folder}")
    private String resourceFolder;
}
