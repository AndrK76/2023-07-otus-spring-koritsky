package ru.otus.andrk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.service.data.CommentService;
import ru.otus.andrk.service.health.HealthService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@CrossOrigin("*")
public class CommentController {
    private final CommentService commentService;

    private final HealthService healthService;

    @GetMapping("/api/v1/book/{book}/comment")
    public List<CommentDto> getCommentsForBook(@PathVariable(name = "book") long bookId) {
        healthService.registerVisitor();
        return commentService.getCommentsForBook(bookId);
    }

    @PostMapping("/api/v1/book/{book}/comment")
    public CommentDto addComment(@PathVariable(name = "book") long bookId,
                                 @RequestBody CommentDto comment) {
        return commentService.addCommentToBook(bookId, comment);
    }

    @PutMapping("/api/v1/comment/{comment}")
    public CommentDto modifyComment(@PathVariable(name = "comment") long commentId,
                                    @RequestBody CommentDto comment) {
        return commentService.modifyComment(commentId, comment);
    }

    @DeleteMapping("/api/v1/comment/{comment}")
    public long deleteComment(@PathVariable(name = "comment") long commentId) {
        commentService.deleteComment(commentId);
        return commentId;
    }
}
