package ru.otus.andrk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Book {
    private final long id;
    private String name;
    private Author author;
    private Genre genre;

    public Book(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
