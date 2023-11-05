package ru.otus.andrk.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.andrk.config.ControllerConfig;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.service.data.BookService;
import ru.otus.andrk.service.data.CommentService;

import java.time.Duration;

@RestController
@Log4j2
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    private final ControllerConfig config;

    @GetMapping("/api/v1/book/{id}/comment")
    public Flux<CommentDto> getCommentsForBook(@PathVariable(name = "id") String bookId){
        return commentService.getCommentsByBookId(bookId)
                .delayElements(Duration.ofMillis(config.getListDelayInMs()),config.getScheduler());
    }

    @DeleteMapping("/api/v1/comment/{id}")
    public Mono<Void> deleteCommentById(@PathVariable(name = "id") String commentId){
        return commentService.deleteComment(commentId);
    }
}
