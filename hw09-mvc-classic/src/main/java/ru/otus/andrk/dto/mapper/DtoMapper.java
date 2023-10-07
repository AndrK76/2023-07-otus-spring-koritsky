package ru.otus.andrk.dto.mapper;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.BookWithCommentsDto;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.CommentOnBookDto;
import ru.otus.andrk.dto.GenreDto;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.model.Genre;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class DtoMapper {
    public AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }

    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public BookDto toDto(Book book) {
        var ret = BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .authorId(Optional.ofNullable(book.getAuthor()).map(Author::getId).orElse(null))
                .authorName(Optional.ofNullable(book.getAuthor()).map(Author::getName).orElse(null))
                .genreId(Optional.ofNullable(book.getGenre()).map(Genre::getId).orElse(null))
                .genreName(Optional.ofNullable(book.getGenre()).map(Genre::getName).orElse(null))
                .build();
        return ret;
    }

    public BookWithCommentsDto toDtoWithComments(Book book) {
        var ret = BookWithCommentsDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .comments(
                        book.getComments() == null
                                ? new ArrayList<>()
                                : book.getComments().stream()
                                .map(this::toDto).toList())
                .build();
        return ret;
    }


    @Transactional(readOnly = true)
    public CommentOnBookDto toDtoWithBook(Comment comment) {
        Book book = comment.getBook();
        return new CommentOnBookDto(
                comment.getId(),
                comment.getText(),
                book.getId(),
                book.getName(),
                book.getAuthor() == null ? "" : book.getAuthor().getName(),
                book.getGenre() == null ? "" : book.getGenre().getName());
    }

    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }


}
