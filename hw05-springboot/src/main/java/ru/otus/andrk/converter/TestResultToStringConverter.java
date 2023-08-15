package ru.otus.andrk.converter;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.otus.andrk.model.TestResult;
import ru.otus.andrk.service.i18n.MessageService;

@Service
public class TestResultToStringConverter implements Converter<TestResult, String> {

    private final MessageService messageService;

    public TestResultToStringConverter(@Lazy MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public String convert(TestResult result) {
        return messageService.getMessage("RESULT_MESSAGE",
                result.getStudent().firstName(), result.getStudent().lastName(),
                result.getSuccessCount(), result.getFailCount());
    }
}
