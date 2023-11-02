package ru.otus.andrk.controller.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.andrk.config.ExceptionHandlerConfig;
import ru.otus.andrk.dto.mapper.ApiErrorMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.web.reactive.function.server.RequestPredicates.all;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
@Order(-2)
@Log4j2
public class CustomExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final Scheduler scheduler;

    private final ApiErrorMapper mapper;

    public CustomExceptionHandler(ExceptionHandlerConfig config) {
        super(config.getErrorAttributes(), config.getResources(), config.getApplicationContext());
        this.setMessageWriters(config.getConfigurer().getWriters());
        this.setViewResolvers(config.getViewResolvers());
        this.scheduler = config.getScheduler();
        this.mapper = config.getMapper();
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        MediaType[] jsons = new MediaType[]{MediaType.APPLICATION_JSON, MediaType.APPLICATION_NDJSON};
        return route(contentType(jsons), this::formatJsonResponse)
                .and(route(all(), this::formatTextResponse));
    }

    private Mono<ServerResponse> formatJsonResponse(ServerRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        int status = (int) Optional.ofNullable(errorAttributes.get("status")).orElse(500);

        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(mapper.fromErrorAttributes(errorAttributes)))
                .publishOn(scheduler)
                .doOnNext(l -> log.debug("formatJsonResponse"))
                ;
    }

    private Mono<ServerResponse> formatTextResponse(ServerRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        int status = (int) Optional.ofNullable(errorAttributes.get("status")).orElse(500);

        return ServerResponse
                .status(status)
                .contentType(MediaType.TEXT_HTML)
                .render("error", makeMapForTextModel(errorAttributes))
                .publishOn(scheduler)
                .doOnNext(l-> log.debug("formatTextResponse"));
    }

    private Map<String, Object> makeMapForTextModel(Map<String, Object> errorAttributes) {
        Map<String, Object> model = new HashMap<>();
        model.put("info", mapper.fromErrorAttributes(errorAttributes));
        return model;
    }
}
