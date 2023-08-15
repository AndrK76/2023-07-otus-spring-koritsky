package ru.otus.andrk.service.i18n;

public interface MessageService {
    String getMessage(String key, Object... args);
}
