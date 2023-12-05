package ru.otus.andrk.config.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.mapper.ApiErrorDtoConverter;
import ru.otus.andrk.exception.OtherLibraryManipulationException;

@ControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class CustomErrorHandler {

    private final ApiErrorDtoConverter dtoConverter;

    @ExceptionHandler(OtherLibraryManipulationException.class)
    public ResponseEntity<ApiErrorDto> otherLibraryManipulationErr(OtherLibraryManipulationException e, WebRequest request){
        var ret = dtoConverter.fromOtherLibraryManipulationException(e, request);
        return ResponseEntity.status(ret.getStatus()).body(ret);
    }
}
