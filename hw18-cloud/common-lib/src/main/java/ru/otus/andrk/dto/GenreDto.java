package ru.otus.andrk.dto;

import java.io.Serializable;

public record GenreDto(
        long id, String name
) implements Serializable {
}
