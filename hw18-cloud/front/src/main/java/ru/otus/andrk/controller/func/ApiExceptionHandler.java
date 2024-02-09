package ru.otus.andrk.controller.func;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.mapper.ApiErrorDtoMapper;

@ControllerAdvice(basePackages = "ru.otus.andrk.controller.func")
@RequiredArgsConstructor
@Log4j2
public class ApiExceptionHandler {

    private final ApiErrorDtoMapper mapper;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorDto> otherLibErr(RuntimeException e) {
        log.error("{}: {}", e.getClass(), e.getMessage());
        var ret = mapper.fromOtherError(e);
        return ResponseEntity.status(ret.getStatus()).body(ret);
    }
}
