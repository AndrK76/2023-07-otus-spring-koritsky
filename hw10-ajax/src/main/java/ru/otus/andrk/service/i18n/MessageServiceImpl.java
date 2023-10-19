package ru.otus.andrk.service.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageSource messageSource;


    @Override
    public Map<String, String> getMessages(Locale locale, List<String> messageKeys) {
        return messageKeys.stream()
                .collect(Collectors.toMap(k -> k, v -> {
                    try {
                        return messageSource.getMessage(v, null, locale);
                    } catch (NoSuchMessageException e) {
                        return "";
                    }
                }));
    }

    @Override
    public String getMessage(Locale locale, String messageKey, Object[] args) {
        return messageSource.getMessage(messageKey,args, locale);
    }
}

