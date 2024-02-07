package ru.otus.andrk.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomInfoContributor implements InfoContributor {
    private final ServletWebServerApplicationContext webContext;

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("port",webContext.getWebServer().getPort());
    }
}
