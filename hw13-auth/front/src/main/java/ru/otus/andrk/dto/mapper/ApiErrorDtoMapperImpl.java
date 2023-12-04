package ru.otus.andrk.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.service.i18n.MessageService;

import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApiErrorDtoMapperImpl implements ApiErrorDtoMapper {

    private final MessageService messageService;
    @Override
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
        ret.setDetailMessage(new ApiErrorDto.MessagePair("", path));
        return ret;
    }

    @Override
    public ApiErrorDto fromOtherError(RuntimeException e) {
        var ret = new ApiErrorDto(new Date(), 500);
        setStatus(ret);
        var messageKey = "other-error";
        return makeApiErrorDto(e, ret, messageKey);    }

    private String getStatusMessageKey(int status) {
        return "error.status." + status;
    }

    private void setStatus(ApiErrorDto messageObj) {
        var messageKey = getStatusMessageKey(messageObj.getStatus());
        messageObj.setStatusMessage(
                new ApiErrorDto.MessagePair(messageKey,
                        messageService.getMessageInDefaultLocale(messageKey, null)));
    }

    private ApiErrorDto makeApiErrorDto(RuntimeException ex, ApiErrorDto ret, String messageKey) {
        ret.setErrorMessage(new ApiErrorDto.MessagePair(messageKey,
                messageService.getMessageInDefaultLocale(messageKey, null)));
        if (ex.getCause() != null) {
            var message = StringUtils.hasLength(ex.getCause().getMessage())
                    ? ex.getCause().getMessage()
                    : ex.getCause().getClass().getSimpleName();
            ret.setDetailMessage(new ApiErrorDto.MessagePair("", message));
        }
        return ret;
    }
}
