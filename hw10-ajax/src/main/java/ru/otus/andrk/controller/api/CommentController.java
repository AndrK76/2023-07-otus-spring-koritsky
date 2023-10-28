package ru.otus.andrk.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.CommentOnBookDto;
import ru.otus.andrk.service.data.CommentService;

@RestController
@Log4j2
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/v1/comment/{id}")
    public CommentDto getById(@PathVariable(name = "id") long commentId) {
        return commentService.getCommentById(commentId);
    }

    @PostMapping("/api/v1/comment")
    public CommentDto addComment(@RequestBody @Valid CommentOnBookDto comment) {
        return commentService.addCommentForBook(comment.getBookId(), comment.getText());
    }

    @PutMapping("/api/v1/comment/{id}")
    public CommentDto modifyComment(
            @PathVariable(name = "id") long commentId,
            @RequestBody @Valid CommentDto comment) {
        return commentService.modifyComment(commentId, comment.text());
    }

    @DeleteMapping("/api/v1/comment/{id}")
    public void deleteComment(@PathVariable(name = "id") long commentId) {
        commentService.deleteComment(commentId);
    }


}
