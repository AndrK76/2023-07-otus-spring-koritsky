package ru.otus.andrk.dto.mapper;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.MessagePair;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;

import java.util.Map;

public interface ApiErrorMapper {
    ApiErrorDto fromErrorAttributes(Map<String, Object> errAttrs);

    Mono<ApiErrorDto> fromOtherError(OtherLibraryManipulationException e);

    Mono<ApiErrorDto> fromKnownError(KnownLibraryManipulationException e);

    Mono<ApiErrorDto> fromStatusError(ResponseStatusException e);

    Map<String, MessagePair> fromNotValidArgument(MethodArgumentNotValidException e);
}