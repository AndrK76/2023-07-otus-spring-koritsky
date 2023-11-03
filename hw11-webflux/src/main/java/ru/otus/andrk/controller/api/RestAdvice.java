package ru.otus.andrk.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.client.HttpStatusCodeException;
import reactor.core.publisher.Mono;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.mapper.ApiErrorMapper;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;

@RestControllerAdvice(basePackages = "ru.otus.andrk.controller.api")
@RequiredArgsConstructor
@Log4j2
public class RestAdvice {
    private final ApiErrorMapper mapper;

    @ExceptionHandler(OtherLibraryManipulationException.class)
    public Mono<ResponseEntity<ApiErrorDto>> otherLibErr(OtherLibraryManipulationException e) {
        return mapper.fromOtherError(e)
                .doOnNext(r -> log.debug("otherLibErr: {}", e.toString()))
                .map(r -> ResponseEntity.status(r.getStatus()).body(r));
    }

    @ExceptionHandler(KnownLibraryManipulationException.class)
    public Mono<ResponseEntity<ApiErrorDto>> knownLibErr(KnownLibraryManipulationException e) {
        return mapper.fromKnownError(e)
                .doOnNext(r -> log.debug("knownLibErr: {}", e.toString()))
                .map(r -> ResponseEntity.status(r.getStatus()).body(r));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ApiErrorDto>> notValidArgErr(WebExchangeBindException e) {
        return mapper.fromNotValidArgument(e)
                .doOnNext(r -> log.debug("argValidationErr: {}", e.toString()))
                .map(r -> ResponseEntity.status(r.getStatus()).body(r));
    }

}
