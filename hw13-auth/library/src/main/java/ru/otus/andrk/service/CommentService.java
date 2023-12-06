package ru.otus.andrk.service;

import ru.otus.andrk.dto.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getCommentsForBook(long bookId);

    CommentDto addCommentToBook(long bookId, CommentDto comment);

    CommentDto modifyComment(long commentId, CommentDto comment);

    void deleteComment(long id);

}
