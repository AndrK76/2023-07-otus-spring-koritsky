package ru.otus.andrk.actuator;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
@Log4j2
public class CustomInfoContributor implements InfoContributor {
    private final ServletWebServerApplicationContext webContext;

    private final DataSource dataSource;

    @Override
    public void contribute(Info.Builder builder) {
        try {
            var dbPlatform = dataSource.getConnection().getMetaData().getDatabaseProductName();
            builder.withDetail("db-type", dbPlatform);
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }
        builder.withDetail("port", webContext.getWebServer().getPort());
    }
}
