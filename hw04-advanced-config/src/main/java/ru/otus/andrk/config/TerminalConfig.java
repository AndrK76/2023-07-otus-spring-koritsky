package ru.otus.andrk.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.boot.TerminalCustomizer;

@Configuration
public class TerminalConfig {
    @Bean
    @ConditionalOnProperty(name = "test-system.run-mode.use-console", havingValue = "true", matchIfMissing = false)
    public TerminalCustomizer terminalCustomizerCharset(){
        return builder -> builder.encoding(System.console().charset());
    }
}
