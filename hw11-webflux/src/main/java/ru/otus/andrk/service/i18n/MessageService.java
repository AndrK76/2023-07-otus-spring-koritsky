package ru.otus.andrk.service.i18n;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface MessageService {
    Mono<Map<String, String>> getMessages(String lang);

    String getMessageInDefaultLocale(String messageKey, Object[] args);

}
