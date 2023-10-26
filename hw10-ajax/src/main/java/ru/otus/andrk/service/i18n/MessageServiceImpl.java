package ru.otus.andrk.service.i18n;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import ru.otus.andrk.config.ApplicationSettings;
import ru.otus.andrk.exception.LocalizationException;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MessageServiceImpl implements MessageService {

    private final MessageSource messageSource;

    private final ApplicationSettings appSettings;

    @Override
    public Map<String, String> getMessages(String lang) {
        try {
            Locale locale = Locale.forLanguageTag(lang);
            return ResourceBundle.getBundle(appSettings.getMessageBundle(), locale).keySet().stream()
                    .collect(Collectors.toMap(k -> k, v -> {
                        try {
                            return messageSource.getMessage(v, null, locale);
                        } catch (NoSuchMessageException e) {
                            log.error(e);
                            return "";
                        }
                    }));
        } catch (Exception e) {
            log.error(e);
            throw new LocalizationException(e);
        }
    }

    @Override
    public String getMessageInDefaultLocale(String messageKey, Object[] args) {
        var locale = new Locale(appSettings.getDefaultLang());
        try {
            return messageSource.getMessage(messageKey, args, locale);
        } catch (NoSuchMessageException e) {
            log.error(e);
            return "";
        }
    }

}

