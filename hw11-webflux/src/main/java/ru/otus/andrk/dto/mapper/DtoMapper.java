package ru.otus.andrk.dto.mapper;

import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.GenreDto;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.model.Genre;


public interface DtoMapper {
    AuthorDto toDto(Author author);

    Author fromDto(AuthorDto dto);

    GenreDto toDto(Genre genre);

    Genre fromDto(GenreDto dto);

    BookDto toDto(Book book);

    CommentDto toDto(Comment comment);

    /*
    BookWithCommentsDto toDtoWithComments(Book book);

    CommentOnBookDto toDtoWithBook(Comment comment);


     */
}
