package ru.otus.andrk.controller.error;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.mapper.ApiErrorMapper;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;

@ControllerAdvice(basePackages = "ru.otus.andrk.controller.api")
@RequiredArgsConstructor
@Log4j2
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final ApiErrorMapper mapper;

    @ExceptionHandler(OtherLibraryManipulationException.class)
    public ResponseEntity<ApiErrorDto> otherLibErr(OtherLibraryManipulationException e) {
        log.debug("otherLibErr: {}",e.toString());
        var ret = mapper.fromOtherError(e);
        return ResponseEntity.status(ret.getStatus()).body(ret);
    }

    @ExceptionHandler(KnownLibraryManipulationException.class)
    public ResponseEntity<ApiErrorDto> knownLibErr(KnownLibraryManipulationException e){
        log.debug("knownLibErr: {}",e.toString());
        var ret = mapper.fromKnownError(e);
        return ResponseEntity.status(ret.getStatus()).body(ret);

    }

}
