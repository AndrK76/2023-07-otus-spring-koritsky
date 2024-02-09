package ru.otus.andrk.service.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.exception.ProcessedException;
import ru.otus.andrk.feign.LibraryAppClient;

@Service
@RequiredArgsConstructor
@Log4j2
public class ValidationServiceFeignBreaked implements ValidationService {

    private final LibraryAppClient libraryClient;

    private final ObjectMapper objectMapper;


    @Override
    @CircuitBreaker(name = "libraryServiceBreaker", fallbackMethod = "validateBookFailBack")
    @Retry(name = "libraryServiceBreaker")
    public ResponseEntity<?> validateBook(BookDto book) {
        return ResponseEntity.ok(libraryClient.validateBook(book));
    }

    @Override
    @CircuitBreaker(name = "libraryServiceBreaker", fallbackMethod = "validateCommentFailBack")
    @Retry(name = "libraryServiceBreaker")
    public ResponseEntity<?> validateComment(CommentDto comment) {
        return ResponseEntity.ok(libraryClient.validateComment(comment));
    }

    private ResponseEntity<?> validateBookFailBack(BookDto book, RuntimeException e) {
        return processValidationResponse(e, "validateBookFailBack", book);
    }

    private ResponseEntity<?> validateCommentFailBack(CommentDto comment, RuntimeException e) {
        return processValidationResponse(e, "validateCommentFailBack", comment);
    }

    private ResponseEntity<?> processValidationResponse(
            RuntimeException e, String methodName, Object originalArgument) {
        if (e instanceof ProcessedException ex) {
            return ResponseEntity.status(ex.getData().getStatus()).body(ex.getData());
        } else {
            log.debug("{}: {}", methodName, e.getMessage());
            return ResponseEntity.ok(originalArgument);
        }
    }
}
