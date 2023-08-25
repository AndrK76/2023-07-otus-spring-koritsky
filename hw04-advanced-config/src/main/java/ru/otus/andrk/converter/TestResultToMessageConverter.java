package ru.otus.andrk.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.otus.andrk.model.TestResult;
import ru.otus.andrk.service.dialog.Message;


@Service
public class TestResultToMessageConverter implements Converter<TestResult, Message> {


    @Override
    public Message convert(TestResult result) {
        return new Message("RESULT_MESSAGE",
                result.getStudent().firstName(), result.getStudent().lastName(),
                result.getSuccessCount(), result.getFailCount());
    }
}
