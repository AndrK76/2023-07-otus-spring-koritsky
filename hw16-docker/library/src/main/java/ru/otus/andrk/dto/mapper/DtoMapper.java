package ru.otus.andrk.dto.mapper;

import org.springframework.stereotype.Component;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.GenreDto;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.model.Genre;

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
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .authorId(Optional.ofNullable(book.getAuthor()).map(Author::getId).orElse(null))
                .authorName(Optional.ofNullable(book.getAuthor()).map(Author::getName).orElse(null))
                .genreId(Optional.ofNullable(book.getGenre()).map(Genre::getId).orElse(null))
                .genreName(Optional.ofNullable(book.getGenre()).map(Genre::getName).orElse(null))
                .build();
    }

    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }


}
