package ru.otus.andrk.service.i18n;

import org.springframework.stereotype.Component;
import ru.otus.andrk.dao.ContentLoadException;

import java.util.List;

@Component
public class ResourceServiceImpl implements ResourceService {

    private final LocaleProvider localeProvider;

    public ResourceServiceImpl(LocaleProvider localeProvider) {
        this.localeProvider = localeProvider;
    }

    @Override
    public String getLocalizedResourceName(String resourceRoot, String resourceName) {
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
        throw new ContentLoadException("Resource not found");
    }
}
