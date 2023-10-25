package ru.otus.andrk.dto.mapper;

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


public interface DtoMapper {
    AuthorDto toDto(Author author);

    GenreDto toDto(Genre genre);

    BookDto toDto(Book book);

    BookWithCommentsDto toDtoWithComments(Book book);

    CommentOnBookDto toDtoWithBook(Comment comment);

    CommentDto toDto(Comment comment);

}
