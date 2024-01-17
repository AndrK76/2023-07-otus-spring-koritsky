package ru.otus.andrk.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.mapper.ApiErrorDtoMapper;

@ControllerAdvice(basePackages = "ru.otus.andrk.controller.rest")
@RequiredArgsConstructor
@Log4j2
public class ApiExceptionHandler {

    private final ApiErrorDtoMapper mapper;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorDto> otherLibErr(RuntimeException e) {
        var ret = mapper.fromOtherError(e);
        return ResponseEntity.status(ret.getStatus()).body(ret);
    }
}
