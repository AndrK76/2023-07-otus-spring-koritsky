package ru.otus.andrk.service;

import ru.otus.andrk.model.Author;

import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthors();

    Author addAuthor(String authorName);

    Author getAuthorById(long authorId);

}
