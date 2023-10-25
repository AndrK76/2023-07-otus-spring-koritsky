package ru.otus.andrk.dto.mapper;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.MessagePair;
import ru.otus.andrk.exception.BuBuException;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.exception.converter.ExceptionToStringMapper;
import ru.otus.andrk.service.i18n.MessageService;

import java.util.Date;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class ApiErrorMapperImpl implements ApiErrorMapper {

    private final MessageService messageService;

    private final ExceptionToStringMapper exceptionMapper;

    public ApiErrorDto fromErrorAttributes(Map<String, Object> errAttrs) {
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
        setStatus(ret);
        ret.setDetailMessage(new MessagePair("", path));
        return ret;
    }

    @Override
    public ApiErrorDto fromOtherError(OtherLibraryManipulationException e) {
        RuntimeException ex = e;
        log.debug("fromOtherError: {}", e.toString());
        var ret = new ApiErrorDto(new Date(), 500);
        setStatus(ret);
        var messageKey = "known-error.other-manipulation-error";
        if (e.getCause() instanceof BuBuException){
            messageKey = "known-error.bubu-error";
            ex = (BuBuException)e.getCause();
        }
        return makeApiErrorDto(ex, ret, messageKey);
    }

    @Override
    public ApiErrorDto fromKnownError(KnownLibraryManipulationException e) {
        log.debug("fromKnownError: {}", e.toString());
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
