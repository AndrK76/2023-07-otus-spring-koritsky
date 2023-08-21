package ru.otus.andrk.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.otus.andrk.model.TestResult;
import ru.otus.andrk.service.i18n.MessageProvider;

@Service
public class TestResultToStringConverter implements Converter<TestResult, String> {

    private final MessageProvider messageProvider;

    public TestResultToStringConverter(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    @Override
    public String convert(TestResult result) {
        return messageProvider.getMessage("RESULT_MESSAGE",
                result.getStudent().firstName(), result.getStudent().lastName(),
                result.getSuccessCount(), result.getFailCount());
    }
}
