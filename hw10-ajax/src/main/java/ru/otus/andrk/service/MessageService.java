package ru.otus.andrk.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface MessageService {
    Map<String,String> getMessages(Locale locale, List<String> messageKeys);
}
