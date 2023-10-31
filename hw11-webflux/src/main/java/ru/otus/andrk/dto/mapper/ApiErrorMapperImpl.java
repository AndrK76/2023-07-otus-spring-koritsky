package ru.otus.andrk.dto.mapper;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.MessagePair;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.exception.converter.ExceptionToStringMapper;
import ru.otus.andrk.service.i18n.MessageService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class ApiErrorMapperImpl implements ApiErrorMapper {

    private final MessageService messageService;

    private final ExceptionToStringMapper exceptionMapper;

    private final Scheduler scheduler;


    @Override
    public Mono<ApiErrorDto> fromErrorAttributes(Map<String, Object> errAttrs) {
        return Mono.just(makeErrorDtoFromErrorAttributes(errAttrs))
                .publishOn(scheduler)
                .doOnNext(r -> log.debug("fromErrorAttributes"));
    }

    @Override
    public Mono<ApiErrorDto> fromOtherError(OtherLibraryManipulationException e) {
        return Mono.just(makeFromOtherError(e)).publishOn(scheduler)
                .doOnNext(l->log.debug("fromOtherError: {}",e.toString()));
    }

    @Override
    public Mono<ApiErrorDto> fromKnownError(KnownLibraryManipulationException e) {
        return Mono.just(makeFromKnownError(e)).publishOn(scheduler)
                .doOnNext(l-> log.debug("fromKnownError: {}", e.toString()));
    }

    @Override
    public Map<String, MessagePair> fromNotValidArgument(MethodArgumentNotValidException e) {
        Map<String, MessagePair> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessageKey = error.getDefaultMessage();
            //String errorMessage = messageService.getMessageInDefaultLocale(errorMessageKey, null);
            errors.put(fieldName, new MessagePair(errorMessageKey, "errorMessage"));
        });
        return errors;
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
        RuntimeException ex = e;
        var ret = new ApiErrorDto(new Date(), 500);
        setStatus(ret);
        var messageKey = "known-error.other-manipulation-error";
        return makeApiErrorDto(ex, ret, messageKey);
    }

    private ApiErrorDto makeFromKnownError(KnownLibraryManipulationException e) {
        var ret = new ApiErrorDto(new Date(), 400);
        setStatus(ret);
        var messageKey = exceptionMapper.getExceptionMessage(e);
        return makeApiErrorDto(e, ret, messageKey);
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
