package ru.otus.andrk.service.testresults;

import ru.otus.andrk.model.TestResult;

public interface TestResultConverterService {
    String resultToString(TestResult result);
}
