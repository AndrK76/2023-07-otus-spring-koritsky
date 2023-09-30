package ru.otus.andrk.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DtoMapper {
    public BookDto bookToDto(Book book) {
        var ret = BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .build();
        return ret;
    }

    public CommentOnBookDto commentToCommentOnBookDto(Comment comment) {
        Book book = comment.getBook();
        return new CommentOnBookDto(
                comment.getId(),
                comment.getText(),
                book.getId(),
                book.getName(),
                book.getAuthor() == null ? "" : book.getAuthor().getName(),
                book.getGenre() == null ? "" : book.getGenre().getName());
    }

    public CommentDto commentToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }


    public BookWithCommentsDto bookToDtoWithComments(Book book, List<Comment> bookComments) {
        var ret = BookWithCommentsDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .build();
        ret.setComments(bookComments.stream()
                .map(this::commentToDto).toList());
        return ret;
    }
}
