package ru.otus.andrk.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class CsvConfig {
    @Value("${questions.csv-delimiter}")
    private String csvDelimiter;
}
