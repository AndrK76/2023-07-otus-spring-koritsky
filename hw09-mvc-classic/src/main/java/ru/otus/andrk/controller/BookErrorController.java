package ru.otus.andrk.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.converter.ExceptionToStringMapper;


@Controller
@ControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class BookErrorController implements ErrorController {

    private final ExceptionToStringMapper exceptionMapper;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(KnownLibraryManipulationException.class)
    public String knownError(KnownLibraryManipulationException e, Model model) {
        model.addAttribute("status", HttpStatus.BAD_REQUEST);
        model.addAttribute("message", exceptionMapper.getExceptionMessage(e));
        return "known_error";
    }

    @RequestMapping("/error")
    public String statusException(Model model, HttpServletRequest request) {
        var status = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        model.addAttribute("status", HttpStatus.resolve(status));
        model.addAttribute("message", status);
        return "known_error";
    }
}
