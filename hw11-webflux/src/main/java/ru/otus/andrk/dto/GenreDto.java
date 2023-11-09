package ru.otus.andrk.dto;

import java.io.Serializable;

public record GenreDto(
        String id, String name
) implements Serializable {
}
