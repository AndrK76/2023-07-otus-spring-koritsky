package ru.otus.andrk.dto.mapper;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.service.i18n.MessageService;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class ApiErrorMapper {

    private final MessageService messageService;

    public ApiErrorDto fromErrorAttributes(Map<String, Object> errAttrs, String lang) {
        log.debug(lang);
        Object errTimestamp = errAttrs.get("timestamp");
        Object errStatus = errAttrs.get("status");
        Object errMessage = errAttrs.get("error");
        Date timestamp = errTimestamp instanceof Date
                ? (Date) errTimestamp
                : new Date();
        int status = errStatus instanceof Integer
                ? (Integer) errStatus
                : 500;
        var ret = new ApiErrorDto(timestamp, status);
        ret.setStatusMessage(
                errMessage instanceof String && !Strings.isNullOrEmpty((String) errMessage)
                        ? (String) errMessage
                        : HttpStatus.valueOf(status).name());
        ret.setStatusMessageKey(getStatusMessage(ret.getStatus()));

        try {
            ret.setStatusMessageLocalized(
                    messageService.getMessage(Locale.forLanguageTag(lang),
                            ret.getStatusMessageKey(), null));
        } catch (NoSuchMessageException e) {
            log.error(e);
        }
        return ret;
    }

    private String getStatusMessage(int status) {
        return "error.status." + status;
    }


}
