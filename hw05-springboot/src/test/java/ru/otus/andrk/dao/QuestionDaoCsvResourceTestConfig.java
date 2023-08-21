package ru.otus.andrk.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.andrk.config.QuestionsDaoCsvConfig;
import ru.otus.andrk.service.i18n.ResourceProvider;

import static org.mockito.Mockito.mock;

@Configuration
public class QuestionDaoCsvResourceTestConfig {

    @Bean
    public QuestionsDaoCsvConfig questionsDaoCsvConfig(){
        return mock(QuestionsDaoCsvConfig.class);
    }

    @Bean
    public ResourceProvider resourceProvider(){
        return mock(ResourceProvider.class);
    }

}
