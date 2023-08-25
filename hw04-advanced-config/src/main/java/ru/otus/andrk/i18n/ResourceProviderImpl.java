package ru.otus.andrk.i18n;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResourceProviderImpl implements ResourceProvider {

    private final LocaleProvider localeProvider;

    public ResourceProviderImpl(LocaleProvider localeProvider) {
        this.localeProvider = localeProvider;
    }

    @Override
    public String getResourcePath(String resourceRoot, String resourceName) {
        var loader = getClass().getClassLoader();
        var locale = localeProvider.getCurrent();
        var localizePath = List.of(
                (resourceRoot + "/" + locale.getLanguage() + "/" + locale.getCountry() + "/" + resourceName)
                        .replace("//", "/"),
                resourceRoot + "/" + locale.getLanguage() + "/" + resourceName,
                resourceRoot + "/" + resourceName);
        for (String path : localizePath) {
            if (loader.getResource(path) != null) {
                return path;
            }
        }
        throw new ResourceFindException("Resource not found");
    }
}
