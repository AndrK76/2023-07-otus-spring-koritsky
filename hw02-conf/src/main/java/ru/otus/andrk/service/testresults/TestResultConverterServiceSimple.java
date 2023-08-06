package ru.otus.andrk.service.testresults;

import org.springframework.stereotype.Service;
import ru.otus.andrk.model.TestResult;

@Service
public class TestResultConverterServiceSimple implements TestResultConverterService {
    @Override
    public String resultToString(TestResult result) {
        return String.format("Student: %s %s make %d valid answers and %d fails",
                result.getStudent().firstName(), result.getStudent().lastName(),
                result.getSuccessCount(), result.getFailCount());
    }
}
