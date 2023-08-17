package ru.otus.andrk.converter;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.service.i18n.MessageProvider;
import ru.otus.andrk.service.question.AbstractIncorrectAnswerException;

@Component
public class IncorrectAnswerExceptionToStringConverter implements Converter<AbstractIncorrectAnswerException, String> {

    private final MessageProvider messageProvider;

    public IncorrectAnswerExceptionToStringConverter(@Lazy MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    @Override
    public String convert(AbstractIncorrectAnswerException exception) {
        return messageProvider.getMessage("IncorrectAnswerException." + exception.getClass().getSimpleName());
    }
}
