package ru.otus.andrk.dto.mapper;

import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class DtoMapperImpl implements DtoMapper {
    @Override
    public AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }

    @Override
    public Author fromDto(AuthorDto dto) {
        return new Author(dto.id(), dto.name());
    }

    @Override
    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    @Override
    public Genre fromDto(GenreDto dto) {
        return new Genre(dto.id(), dto.name());
    }

    @Override
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

    @Override
    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }

    /*
    @Override
    public BookWithCommentsDto toDtoWithComments(Book book) {
        return BookWithCommentsDto.builder()
                .id(book.getId())
                .name(book.getName())
                .authorName(Optional.ofNullable(book.getAuthor()).map(Author::getName).orElse(null))
                .genreName(Optional.ofNullable(book.getGenre()).map(Genre::getName).orElse(null))
                .comments(
                        book.getComments() == null
                                ? new ArrayList<>()
                                : book.getComments().stream()
                                .map(this::toDto).toList())
                .build();
    }

    @Override
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

    @Override
    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }

     */
}
