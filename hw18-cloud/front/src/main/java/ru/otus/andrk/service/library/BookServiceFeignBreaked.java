package ru.otus.andrk.service.library;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.mapper.ApiErrorDtoMapper;
import ru.otus.andrk.feign.LibraryAppClient;

import java.util.Collections;

import static ru.otus.andrk.service.library.FailResponseHelper.processFailResponse;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookServiceFeignBreaked implements BookService {

    private final LibraryAppClient libraryClient;

    private final ApiErrorDtoMapper mapper;

    @Override
    @CircuitBreaker(name = "libraryServiceBreaker", fallbackMethod = "getAllBooksFailBack")
    @Retry(name = "libraryServiceBreaker")
    public ResponseEntity<?> getAllBooks() {
        return ResponseEntity.ok(libraryClient.getAllBooks());

    }

    @Override
    @CircuitBreaker(name = "libraryServiceBreaker", fallbackMethod = "addBookFailBack")
    @Retry(name = "libraryServiceBreaker")
    public ResponseEntity<?> addBook(BookDto book) {
        return ResponseEntity.ok(libraryClient.addBook(book));
    }

    @Override
    @CircuitBreaker(name = "libraryServiceBreaker", fallbackMethod = "modifyBookFailBack")
    @Retry(name = "libraryServiceBreaker")
    public ResponseEntity<?> modifyBook(long bookId, BookDto book) {
        return ResponseEntity.ok(libraryClient.modifyBook(bookId, book));
    }

    @Override
    @CircuitBreaker(name = "libraryServiceBreaker", fallbackMethod = "deleteBookFailBack")
    @Retry(name = "libraryServiceBreaker")
    public ResponseEntity<?> deleteBook(long bookId) {
        return ResponseEntity.ok(libraryClient.deleteBook(bookId));
    }

    private ResponseEntity<?> getAllBooksFailBack(RuntimeException e) {

        log.debug("getAllBooksFailBack: {}", e.getMessage());
        return ResponseEntity.ok(Collections.emptyList());
    }

    public ResponseEntity<?> addBookFailBack(BookDto book, RuntimeException e) {
        return processFailResponse(e, "addBookFailBack", log, mapper);
    }

    public ResponseEntity<?> modifyBookFailBack(long bookId, BookDto book, RuntimeException e) {
        return processFailResponse(e, "modifyBookFailBack", log, mapper);
    }

    public ResponseEntity<?> deleteBookFailBack(long bookId, RuntimeException e) {
        return processFailResponse(e, "deleteBookFailBack", log, mapper);
    }


}
