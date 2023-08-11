package ru.otus.andrk.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.otus.andrk.model.TestResult;

@Service
public class TestResultToStringConverter implements Converter<TestResult, String> {

    @Override
    public String convert(TestResult result) {
        return String.format("Student: %s %s make %d valid answers and %d fails",
                result.getStudent().firstName(), result.getStudent().lastName(),
                result.getSuccessCount(), result.getFailCount());
    }
}
