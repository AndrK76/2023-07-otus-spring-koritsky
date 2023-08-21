package ru.otus.andrk.service.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class MessageProviderLocalizedImpl implements MessageProvider {

    private final LocaleProvider localeProvider;

    private final MessageSource messageSource;

    public MessageProviderLocalizedImpl(
            LocaleProvider localeProvider,
            @Lazy MessageSource messageSource) {
        this.localeProvider = localeProvider;
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, localeProvider.getCurrent());
    }
}
