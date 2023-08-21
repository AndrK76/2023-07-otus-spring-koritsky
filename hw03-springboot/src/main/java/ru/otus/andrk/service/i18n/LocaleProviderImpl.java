package ru.otus.andrk.service.i18n;

import java.util.Locale;


public class LocaleProviderImpl implements LocaleProvider {

    private final Locale locale;

    public LocaleProviderImpl(String localeTag) {
        this.locale = Locale.forLanguageTag(localeTag);
    }

    @Override
    public Locale getCurrent() {
        return locale;
    }
}
