package ru.otus.andrk.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.converter.ExceptionToStringMapper;

@ControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class ExceptionAdvice {

    private final ExceptionToStringMapper exceptionMapper;


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(KnownLibraryManipulationException.class)
    public String knownError(KnownLibraryManipulationException e, Model model) {
        model.addAttribute("status", HttpStatus.BAD_REQUEST);
        model.addAttribute("message", exceptionMapper.getExceptionMessage(e));
        return "known_error";
    }
}
