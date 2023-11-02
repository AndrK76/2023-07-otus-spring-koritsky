package ru.otus.andrk.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.scheduler.Scheduler;
import ru.otus.andrk.dto.mapper.ApiErrorMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Getter
public class ExceptionHandlerConfig {
    private final ErrorAttributes errorAttributes;

    private final WebProperties.Resources resources;

    private final ApplicationContext applicationContext;

    private final ServerCodecConfigurer configurer;

    private final List<ViewResolver> viewResolvers;

    private final Scheduler scheduler;

    private final ApiErrorMapper mapper;
}
