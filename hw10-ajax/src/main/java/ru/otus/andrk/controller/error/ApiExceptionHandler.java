package ru.otus.andrk.controller.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(basePackages = "ru.otus.andrk.controller.api")
@Log4j2
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

}
