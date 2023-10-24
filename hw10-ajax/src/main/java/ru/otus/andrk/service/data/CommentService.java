package ru.otus.andrk.service.data;

import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.CommentOnBookDto;

import java.util.List;

public interface CommentService {


    CommentDto getCommentById(long id);

    CommentDto addCommentForBook(long bookId, String text);

    CommentDto modifyComment(long commentId, String newText);

    void deleteComment(long id);

}
