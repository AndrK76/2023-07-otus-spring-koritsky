package ru.otus.andrk.dto.mapper;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ru.otus.andrk.config.DataLayerConfig;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.MessagePair;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.exception.converter.ExceptionToStringMapper;
import ru.otus.andrk.service.i18n.MessageService;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class ApiErrorMapperImpl implements ApiErrorMapper {

    private final MessageService messageService;

    private final ExceptionToStringMapper exceptionMapper;

    private final DataLayerConfig config;

    @Override
    public ApiErrorDto fromErrorAttributes(Map<String, Object> errAttrs) {
        log.debug("fromErrorAttributes");
        return makeErrorDtoFromErrorAttributes(errAttrs);
    }

    @Override
    public Mono<ApiErrorDto> fromOtherError(OtherLibraryManipulationException e) {
        return Mono.just(makeFromOtherError(e))
                .doOnNext(l -> log.debug("fromOtherError: {}", e.toString()))
                .publishOn(config.getScheduler());
    }

    @Override
    public Mono<ApiErrorDto> fromKnownError(KnownLibraryManipulationException e) {
        return Mono.just(makeFromKnownError(e))
                .doOnNext(l -> log.debug("fromKnownError: {}", e.toString()))
                .publishOn(config.getScheduler());
    }

    @Override
    public Mono<ApiErrorDto> fromStatusError(ResponseStatusException e) {
        return Mono.just(makeFromStatusError(e))
                .doOnNext(l -> log.debug("fromStatusError: {}", e.toString()))
                .publishOn(config.getScheduler());
    }

    @Override
    public  Mono<ApiErrorDto> fromNotValidArgument(WebExchangeBindException e) {
        return Mono.just(makeFromNotValidArgument(e))
                .doOnNext(l -> log.debug("fromStatusError: {}", e.toString()))
                .publishOn(config.getScheduler());
    }


    private ApiErrorDto makeErrorDtoFromErrorAttributes(Map<String, Object> errAttrs) {
        Object errTimestamp = errAttrs.get("timestamp");
        Object errStatus = errAttrs.get("status");
        Object errPath = errAttrs.get("path");
        Date timestamp = errTimestamp instanceof Date
                ? (Date) errTimestamp
                : new Date();
        int status = errStatus instanceof Integer
                ? (Integer) errStatus
                : 500;
        var path = errPath instanceof String
                ? (String) errPath
                : null;
        var ret = new ApiErrorDto(timestamp, status);
        ret.setDetailMessage(new MessagePair("", path));
        setStatus(ret);
        return ret;
    }

    private ApiErrorDto makeFromOtherError(OtherLibraryManipulationException e) {
        var ret = new ApiErrorDto(new Date(), 500);
        setStatus(ret);
        var messageKey = "known-error.other-manipulation-error";
        return makeApiErrorDto(e, ret, messageKey);
    }

    private ApiErrorDto makeFromStatusError(ResponseStatusException e) {
        var ret = new ApiErrorDto(new Date(), e.getStatusCode().value());
        setStatus(ret);
        var messageKey = "known-error.other-manipulation-error";
        return makeApiErrorDto(e, ret, messageKey);
    }

    private ApiErrorDto makeFromKnownError(KnownLibraryManipulationException e) {
        var ret = new ApiErrorDto(new Date(), 400);
        setStatus(ret);
        var messageKey = exceptionMapper.getExceptionMessage(e);
        return makeApiErrorDto(e, ret, messageKey);
    }

    private ApiErrorDto makeFromNotValidArgument(WebExchangeBindException e) {
        var ret = new ApiErrorDto(new Date(), 400);
        setStatus(ret);
        var messageKey = exceptionMapper.getExceptionMessage(e);
        ret = makeApiErrorDto(e, ret, messageKey);
        Map<String, MessagePair> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessageKey = error.getDefaultMessage();
            String errorMessage = messageService.getMessageInDefaultLocale(errorMessageKey, null);
            errors.put(fieldName, new MessagePair(errorMessageKey, errorMessage));
        });
        ret.setDetails((Serializable) errors);
        return ret;
    }


    private String getStatusMessageKey(int status) {
        return "error.status." + status;
    }

    private void setStatus(ApiErrorDto messageObj) {
        var messageKey = getStatusMessageKey(messageObj.getStatus());
        messageObj.setStatusMessage(
                new MessagePair(messageKey,
                        messageService.getMessageInDefaultLocale(messageKey, null)));
    }

    private ApiErrorDto makeApiErrorDto(RuntimeException ex, ApiErrorDto ret, String messageKey) {
        ret.setErrorMessage(new MessagePair(messageKey,
                messageService.getMessageInDefaultLocale(messageKey, null)));
        if (ex.getCause() != null) {
            var message = Strings.isNullOrEmpty(ex.getCause().getMessage())
                    ? ex.getCause().getClass().getSimpleName()
                    : ex.getCause().getMessage();
            ret.setDetailMessage(new MessagePair("", message));
        }
        return ret;
    }

}
