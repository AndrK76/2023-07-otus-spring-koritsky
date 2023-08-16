package ru.otus.andrk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.andrk.service.dialogs.DialogService;
import ru.otus.andrk.service.dialogs.DialogServiceStreamImpl;

@Configuration
public class DialogServiceConfig {

    @Bean
    public DialogService dialogService() {
        return new DialogServiceStreamImpl(System.out, System.in);
    }
}
