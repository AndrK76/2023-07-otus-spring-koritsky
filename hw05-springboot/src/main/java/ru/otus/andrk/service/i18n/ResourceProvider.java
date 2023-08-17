package ru.otus.andrk.service.i18n;

public interface ResourceProvider {
    String getResourcePath(String resourceRoot, String resourceName);
}
