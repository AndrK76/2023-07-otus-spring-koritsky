package ru.otus.andrk.dto.mapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;

import java.util.Map;

public interface ApiErrorDtoConverter {
    ApiErrorDto fromErrorAttributes(Map<String, Object> errAttrs);

    ApiErrorDto fromErrorResponse(HttpServletRequest request, HttpServletResponse response,
                                  Exception exception, HttpStatus status);

    ApiErrorDto fromOtherLibraryManipulationException(OtherLibraryManipulationException e, WebRequest request);

    ApiErrorDto fromKnownLibraryManipulationException(KnownLibraryManipulationException e, WebRequest request);

    ApiErrorDto fromMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request);
}
