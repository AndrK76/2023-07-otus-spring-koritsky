package ru.otus.andrk.service.main;

import ru.otus.andrk.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> getAllAuthors();

    AuthorDto addAuthor(String authorName);

    AuthorDto getAuthorById(long authorId);

    AuthorDto getAuthorByName(String name);

}
