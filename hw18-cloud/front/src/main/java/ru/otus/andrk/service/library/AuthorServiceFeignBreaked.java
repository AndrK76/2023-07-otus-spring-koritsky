package ru.otus.andrk.service.library;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.andrk.feign.LibraryAppClient;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthorServiceFeignBreaked implements AuthorService {

    private final LibraryAppClient libraryClient;

    @Override
    @CircuitBreaker(name = "libraryServiceBreaker", fallbackMethod = "getAuthorsFailBack")
    @Retry(name = "libraryServiceBreaker")
    public ResponseEntity<?> getAuthors() {
        return ResponseEntity.ok(libraryClient.getAuthors());
    }

    private ResponseEntity<?> getAuthorsFailBack(RuntimeException e) {
        log.debug("getAuthorsFailBack: {}", e.getMessage());
        return ResponseEntity.ok(Collections.emptyList());
    }
}
