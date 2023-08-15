package ru.otus.andrk.service.i18n;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageServiceLocalizedImpl implements MessageService {

    private final LocaleProvider localeProvider;

    private final MessageSource messageSource;

    public MessageServiceLocalizedImpl(LocaleProvider localeProvider, MessageSource messageSource) {
        this.localeProvider = localeProvider;
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, localeProvider.getCurrent());
    }
}
