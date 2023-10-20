package ru.otus.andrk.service.i18n;

import java.util.Locale;
import java.util.Map;

public interface MessageService {
    Map<String,String> getMessages(Locale locale);

    String getMessageInDefaultLocale(String messageKey, Object[] args);

}
