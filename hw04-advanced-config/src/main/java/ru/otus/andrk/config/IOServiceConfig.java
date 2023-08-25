package ru.otus.andrk.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.andrk.service.io.IOService;
import ru.otus.andrk.service.io.IOServiceStreamImpl;

import java.nio.charset.Charset;

@Configuration
public class IOServiceConfig {

    @Bean
    @ConditionalOnProperty(name = "test-system.run-mode.use-console", havingValue = "false", matchIfMissing = true)
    public IOService ioServiceDefault() {
        return new IOServiceStreamImpl(System.out, System.in, Charset.defaultCharset());
    }

    @Bean
    @ConditionalOnProperty(name = "test-system.run-mode.use-console", havingValue = "true", matchIfMissing = false)
    public IOService ioServiceConsole() {
        return new IOServiceStreamImpl(System.out, System.in, System.console().charset());
    }

}
