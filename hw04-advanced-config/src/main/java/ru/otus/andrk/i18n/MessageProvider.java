package ru.otus.andrk.i18n;

public interface MessageProvider {
    String getMessage(String key, Object... args);
}
