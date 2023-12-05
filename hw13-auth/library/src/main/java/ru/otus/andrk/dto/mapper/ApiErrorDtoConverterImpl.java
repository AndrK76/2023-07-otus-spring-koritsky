package ru.otus.andrk.dto.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.exception.converter.ExceptionToStringMapper;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class ApiErrorDtoConverterImpl implements ApiErrorDtoConverter {

    private final ObjectMapper objectMapper;

    private final ExceptionToStringMapper exceptionMapper;

    @Override
    public ApiErrorDto fromErrorAttributes(Map<String, Object> errAttrs) {
        Object errTimestamp = errAttrs.get("timestamp");
        Object errStatus = errAttrs.get("status");
        Object errPath = errAttrs.get("path");
        int status = errStatus instanceof Integer
                ? (Integer) errStatus
                : 500;
        var path = errPath instanceof String
                ? (String) errPath
                : null;
        var ret = makeDtoBase(status);
        assert HttpStatus.resolve(status) != null;
        ret.setStatusMessage(
                new ApiErrorDto.MessagePair("error.status." + status,
                        HttpStatus.resolve(status).getReasonPhrase())
        );
        ret.setDetailMessage(
                new ApiErrorDto.MessagePair("", "path: " + path)
        );
        return ret;
    }

    @Override
    public ApiErrorDto fromErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception exception, HttpStatus status) {
        response.setStatus(status.value());
        var ret = makeDtoFromHttpRequest(status.value(), request);
        ret.setErrorMessage(
                new ApiErrorDto.MessagePair("", exception.getMessage())
        );
        try {
            response.getOutputStream()
                    .println(objectMapper.writeValueAsString(ret));
        } catch (IOException e) {
            log.error(e);
        }
        return ret;
    }

    @Override
    public ApiErrorDto fromOtherLibraryManipulationException(OtherLibraryManipulationException e, WebRequest request) {
        var ret = makeDtoFromWebRequest(500, request);
        ret.setErrorMessage(
                new ApiErrorDto.MessagePair(exceptionMapper.getExceptionMessage(e), e.getMessage())
        );
        return ret;
    }

    private ApiErrorDto makeDtoFromHttpRequest(int status, HttpServletRequest request) {
        var ret = makeDtoBase(status);
        ret.setDetailMessage(
                new ApiErrorDto.MessagePair("", "path: " +
                        (request == null ? "" : (request.getRequestURI()))
                ));
        return ret;
    }

    private ApiErrorDto makeDtoFromWebRequest(int status, WebRequest request) {
        var ret = makeDtoBase(status);
        ret.setDetailMessage(
                new ApiErrorDto.MessagePair("", "path: " +
                        (request == null ? "" : ((ServletWebRequest) request).getRequest().getRequestURI())
                ));
        return ret;
    }

    private ApiErrorDto makeDtoBase(int status) {
        var ret = new ApiErrorDto(new Date(), status);
        ret.setStatusMessage(
                new ApiErrorDto.MessagePair("error.status." + status,
                        HttpStatus.resolve(status).getReasonPhrase())
        );
        return ret;
    }

}
