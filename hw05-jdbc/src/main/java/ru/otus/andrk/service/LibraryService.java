package ru.otus.andrk.service;

import ru.otus.andrk.model.Book;

import java.util.List;

public interface LibraryService {
    LibraryManipulationResult getAllBooks();

    LibraryManipulationResult getBookById(long id);

    LibraryManipulationResult addBook(Book book);

    void modifyBook(Book book);
}
