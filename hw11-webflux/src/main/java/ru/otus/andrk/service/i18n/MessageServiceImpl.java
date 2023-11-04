package ru.otus.andrk.service.i18n;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.andrk.config.ControllerConfig;
import ru.otus.andrk.config.DataLayerConfig;
import ru.otus.andrk.config.LocalizationConfig;
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

    private final LocalizationConfig config;

    @Override
    public Mono<Map<String, String>> getMessages(String lang) {
        try {
            Locale locale = Locale.forLanguageTag(lang);
            return Mono.just(ResourceBundle.getBundle(config.getMessageBundle(), locale).keySet().stream()
                    .collect(Collectors.toMap(k -> k, v -> {
                        try {
                            log.debug("get message {} {}", lang, v);
                            return messageSource.getMessage(v, null, locale);
                        } catch (NoSuchMessageException e) {
                            log.error(e);
                            return "";
                        }
                    }))).publishOn(config.getScheduler());
        } catch (Exception e) {
            log.error(e);
            throw new LocalizationException(e);
        }
    }

    @Override
    public String getMessageInDefaultLocale(String messageKey, Object[] args) {
        var locale = new Locale(config.getDefaultLang());
        try {
            return messageSource.getMessage(messageKey, args, locale);
        } catch (NoSuchMessageException e) {
            log.debug(e.getMessage());
            return "";
        }
    }

}

