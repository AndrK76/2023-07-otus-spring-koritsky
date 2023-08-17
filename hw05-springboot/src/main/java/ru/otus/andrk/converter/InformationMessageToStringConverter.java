package ru.otus.andrk.converter;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.model.InformationMessage;
import ru.otus.andrk.service.i18n.MessageProvider;

@Component
public class InformationMessageToStringConverter implements Converter<InformationMessage, String> {

    private final MessageProvider messageProvider;

    public InformationMessageToStringConverter(@Lazy MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    @Override
    public String convert(InformationMessage message) {
        return messageProvider.getMessage("INFORMATION_MESSAGE." + message.toString());
    }
}
