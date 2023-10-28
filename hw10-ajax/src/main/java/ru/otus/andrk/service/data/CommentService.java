package ru.otus.andrk.service.data;

import ru.otus.andrk.dto.CommentDto;

public interface CommentService {


    CommentDto getCommentById(long id);

    CommentDto addCommentForBook(long bookId, String text);

    CommentDto modifyComment(long commentId, String newText);

    void deleteComment(long id);
}
