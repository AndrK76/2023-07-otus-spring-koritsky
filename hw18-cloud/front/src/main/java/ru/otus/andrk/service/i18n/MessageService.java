package ru.otus.andrk.service.i18n;

import java.util.Map;

public interface MessageService {
    Map<String, String> getMessages(String lang);

    String getMessageInDefaultLocale(String messageKey, Object[] args);

}
