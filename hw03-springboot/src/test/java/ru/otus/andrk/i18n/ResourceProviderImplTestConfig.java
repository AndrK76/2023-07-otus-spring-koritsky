package ru.otus.andrk.i18n;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.andrk.config.QuestionsDaoCsvConfig;
import ru.otus.andrk.service.i18n.LocaleProvider;

import static org.mockito.Mockito.mock;

@Configuration
public class ResourceProviderImplTestConfig {

    @Bean
    public QuestionsDaoCsvConfig questionsDaoCsvConfig(){
        return mock(QuestionsDaoCsvConfig.class);
    }

    @Bean
    public LocaleProvider localeProvider(){
        return mock(LocaleProvider.class);
    }
}
