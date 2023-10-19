package ru.otus.andrk.service.main;

import ru.otus.andrk.dto.CommentOnBookDto;

import java.util.List;

public interface CommentService {
    List<CommentOnBookDto> getCommentsForBook(long bookId);

    CommentOnBookDto getCommentById(long id);

    CommentOnBookDto addCommentForBook(long bookId, String text);

    CommentOnBookDto modifyComment(long commentId, String newText);

    void deleteComment(long id);

}
