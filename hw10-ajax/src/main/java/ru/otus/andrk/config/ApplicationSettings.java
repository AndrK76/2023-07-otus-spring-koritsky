package ru.otus.andrk.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationSettings {

    private final String defaultLang;

    private final boolean ebableBuBu;

    private final int bubuFactor;

    public ApplicationSettings(
            @Value("${book-app.default-lang:en}") String defaultLang,
            @Value("${book-app.enable-bubu:false}") boolean ebableBuBu,
            @Value("${book-app.bubu-factor:3}") int bubuFactor) {
        this.defaultLang = defaultLang;
        this.ebableBuBu = ebableBuBu;
        this.bubuFactor = bubuFactor > 0 ? bubuFactor : 3;
    }
}
