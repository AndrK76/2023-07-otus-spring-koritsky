package ru.otus.andrk.service.i18n;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MessageServiceImpl implements MessageService {

    private final MessageSource messageSource;

    private final List<String> allMessageKeys = List.of(
            "book.action-edit", "book.action-view-comments",
            "book.action-delete-book", "other-error",
            "known-error.other-manipulation-error", "error",
            "error.detail", "error.status.400", "error.status.404",
            "error.status.405", "error.status.500",
            "book.message-name-not-empty"
    );

    @Value("${book-app.default-lang:en}")
    private String defaultLang;

    @Override
    public Map<String, String> getMessages(Locale locale) {
        return allMessageKeys.stream()
                .collect(Collectors.toMap(k -> k, v -> {
                    try {
                        return messageSource.getMessage(v, null, locale);
                    } catch (NoSuchMessageException e) {
                        log.error(e);
                        return "";
                    }
                }));
    }

    @Override
    public String getMessageInDefaultLocale(String messageKey, Object[] args) {
        var locale = new Locale(defaultLang);
        try {
            return messageSource.getMessage(messageKey, args, locale);
        } catch (NoSuchMessageException e) {
            log.error(e);
            return "";
        }

    }
}

