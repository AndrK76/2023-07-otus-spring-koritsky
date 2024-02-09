package ru.otus.andrk.service.library;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.mapper.ApiErrorDtoMapper;
import ru.otus.andrk.feign.LibraryAppClient;

import java.util.Collections;

import static ru.otus.andrk.service.library.FailResponseHelper.processFailResponse;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentServiceFeignBreaked implements CommentService {

    private final LibraryAppClient libraryClient;

    private final ApiErrorDtoMapper mapper;

    @Override
    @CircuitBreaker(name = "libraryServiceBreaker", fallbackMethod = "getCommentsForBookFailBack")
    @Retry(name = "libraryServiceBreaker")
    public ResponseEntity<?> getCommentsForBook(long bookId) {
        return ResponseEntity.ok(libraryClient.getCommentsForBook(bookId));
    }

    @Override
    @CircuitBreaker(name = "libraryServiceBreaker", fallbackMethod = "addCommentFailBack")
    @Retry(name = "libraryServiceBreaker")
    public ResponseEntity<?> addComment(long bookId, CommentDto comment) {
        return ResponseEntity.ok(libraryClient.addComment(bookId, comment));
    }

    @Override
    @CircuitBreaker(name = "libraryServiceBreaker", fallbackMethod = "modifyCommentFailBack")
    @Retry(name = "libraryServiceBreaker")
    public ResponseEntity<?> modifyComment(long commentId, CommentDto comment) {
        return ResponseEntity.ok(libraryClient.modifyComment(commentId, comment));
    }

    @Override
    @CircuitBreaker(name = "libraryServiceBreaker", fallbackMethod = "deleteCommentFailBack")
    @Retry(name = "libraryServiceBreaker")
    public ResponseEntity<?> deleteComment(long commentId) {
        return ResponseEntity.ok(libraryClient.deleteComment(commentId));
    }

    private ResponseEntity<?> getCommentsForBookFailBack(long bookId, RuntimeException e) {
        log.debug("getCommentsForBookFailBack: {}", e.getMessage());
        return ResponseEntity.ok(Collections.emptyList());
    }

    private ResponseEntity<?> addCommentFailBack(long bookId, CommentDto comment, RuntimeException e) {
        return processFailResponse(e, "addCommentFailBack", log, mapper);
    }

    private ResponseEntity<?> modifyCommentFailBack(long commentId, CommentDto comment, RuntimeException e) {
        return processFailResponse(e, "modifyCommentFailBack", log, mapper);
    }

    private ResponseEntity<?> deleteCommentFailBack(long commentId, RuntimeException e) {
        return processFailResponse(e, "deleteCommentFailBack", log, mapper);
    }

}
