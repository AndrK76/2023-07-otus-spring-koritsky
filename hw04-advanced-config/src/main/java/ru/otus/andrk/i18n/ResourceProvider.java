package ru.otus.andrk.i18n;

public interface ResourceProvider {
    String getResourcePath(String resourceRoot, String resourceName);
}
