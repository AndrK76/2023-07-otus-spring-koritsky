package ru.otus.andrk.dto;

import java.io.Serializable;

public record AuthorDto(
        long id,
        String name
) implements Serializable {
}
