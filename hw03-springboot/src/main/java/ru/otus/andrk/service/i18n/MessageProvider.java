package ru.otus.andrk.service.i18n;

public interface MessageProvider {
    String getMessage(String key, Object... args);
}
