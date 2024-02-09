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
public class GenreServiceFeignBreaked implements GenreService {

    private final LibraryAppClient libraryClient;

    @Override
    @CircuitBreaker(name = "libraryServiceBreaker", fallbackMethod = "getGenresFailBack")
    @Retry(name = "libraryServiceBreaker")
    public ResponseEntity<?> getGenres() {
        return ResponseEntity.ok(libraryClient.getGenres());
    }

    private ResponseEntity<?> getGenresFailBack(RuntimeException e) {
        log.debug("getGenresFailBack: {}", e.getMessage());
        return ResponseEntity.ok(Collections.emptyList());
    }
}
