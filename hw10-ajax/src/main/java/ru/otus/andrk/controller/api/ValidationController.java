package ru.otus.andrk.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.MessagePair;
import ru.otus.andrk.dto.mapper.ApiErrorMapper;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ValidationController {

    private final ApiErrorMapper mapper;

    @PostMapping(value = "/api/v1/validation/book")
    public ResponseEntity<String> validateBook(
            @RequestBody @Valid BookDto book) {
        log.debug("book: {}", book);
        return ResponseEntity.ok("ok");
    }

    @PostMapping(value = "/api/v1/validation/comment")
    public ResponseEntity<String> validateComment(
            @RequestBody @Valid CommentDto comment) {
        log.debug("comment: {}", comment);
        return ResponseEntity.ok("ok");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, MessagePair> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        var errors = mapper.fromNotValidArgument(ex);
        log.debug("errors: {}", errors);
        return errors;
    }

}
