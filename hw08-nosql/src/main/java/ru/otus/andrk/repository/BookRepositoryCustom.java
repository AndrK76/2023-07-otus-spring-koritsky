package ru.otus.andrk.repository;

import ru.otus.andrk.model.Book;

public interface BookRepositoryCustom {
    Book insertBook(Book book);

    void deleteBook(Long bookId);

    void deleteBook(Book book);
}
