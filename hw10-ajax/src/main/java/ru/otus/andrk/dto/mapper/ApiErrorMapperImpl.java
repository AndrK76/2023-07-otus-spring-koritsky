package ru.otus.andrk.dto.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.service.i18n.MessageService;

import java.util.Date;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class ApiErrorMapperImpl implements ApiErrorMapper {

    private final MessageService messageService;

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
        ret.setStatusMessageKey(getStatusMessageKey(ret.getStatus()));
        ret.setStatusMessage(
                messageService.getMessageInDefaultLocale(
                        ret.getStatusMessageKey(), null));
        ret.setDetailMessage(path);
        return ret;
    }

    private String getStatusMessageKey(int status) {
        return "error.status." + status;
    }


}
