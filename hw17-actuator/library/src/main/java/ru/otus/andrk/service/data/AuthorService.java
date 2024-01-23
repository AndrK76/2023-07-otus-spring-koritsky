package ru.otus.andrk.service.data;

import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<AuthorDto> getAllAuthors();

    Author addAuthor(String authorName);

    Optional<Author> getAuthorByName(String name);

}
