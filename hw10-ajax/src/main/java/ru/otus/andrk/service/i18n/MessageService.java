package ru.otus.andrk.service.i18n;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface MessageService {
    Map<String,String> getMessages(Locale locale, List<String> messageKeys);

    String getMessage(Locale locale, String messageKey, Object[] args);

}
