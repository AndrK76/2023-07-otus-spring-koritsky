package ru.otus.andrk.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.mapper.ApiErrorDtoConverter;

import java.util.List;


@Controller
@Log4j2
public class ErrorController extends AbstractErrorController {

    private final ApiErrorDtoConverter converter;

    public ErrorController(ErrorAttributes errorAttributes,
                           List<ErrorViewResolver> errorViewResolvers,
                           ApiErrorDtoConverter converter) {
        super(errorAttributes);
        this.converter = converter;
    }

    @RequestMapping(path = "/error")
    public ResponseEntity<ApiErrorDto> errorData(HttpServletRequest request) {
        final var status = getStatus(request);
        if (status == HttpStatus.NO_CONTENT) {
            return new ResponseEntity<>(status);
        }
        var errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        log.debug(errorAttributes);
        return new ResponseEntity<>(converter.fromErrorAttributes(errorAttributes), status);
    }


}
