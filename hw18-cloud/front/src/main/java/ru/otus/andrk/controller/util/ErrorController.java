package ru.otus.andrk.controller.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.mapper.ApiErrorDtoMapper;

import java.util.List;


@Controller
@Log4j2
public class ErrorController extends AbstractErrorController {

    private final ApiErrorDtoMapper mapper;

    public ErrorController(ErrorAttributes errorAttributes,
                           List<ErrorViewResolver> errorViewResolvers,
                           ApiErrorDtoMapper errMapper) {
        super(errorAttributes);
        this.mapper = errMapper;
    }

    @RequestMapping(path = "/error", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiErrorDto> errorData(HttpServletRequest request) {
        final var status = getStatus(request);
        if (status == HttpStatus.NO_CONTENT) {
            return new ResponseEntity<>(status);
        }
        var errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        log.debug(errorAttributes);
        return new ResponseEntity<>(mapper.fromErrorAttributes(errorAttributes), status);
    }

    @RequestMapping(path = "/error", produces = MediaType.TEXT_HTML_VALUE)
    public String errorHtml(HttpServletRequest request, HttpServletResponse response) {
        final var status = getStatus(request);
        var errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        log.debug(errorAttributes);
        return "redirect:/";
    }
}
