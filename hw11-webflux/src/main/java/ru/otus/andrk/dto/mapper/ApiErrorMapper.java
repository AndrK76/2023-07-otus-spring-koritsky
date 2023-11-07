package ru.otus.andrk.dto.mapper;

import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;

import java.util.Map;

public interface ApiErrorMapper {
    ApiErrorDto fromErrorAttributes(Map<String, Object> errAttrs);

    Mono<ApiErrorDto> fromOtherError(OtherLibraryManipulationException e);

    Mono<ApiErrorDto> fromKnownError(KnownLibraryManipulationException e);

    Mono<ApiErrorDto> fromStatusError(ResponseStatusException e);

    Mono<ApiErrorDto> fromNotValidArgument(WebExchangeBindException e);
}
