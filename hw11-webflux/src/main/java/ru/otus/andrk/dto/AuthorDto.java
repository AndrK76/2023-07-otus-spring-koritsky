package ru.otus.andrk.dto;

import java.io.Serializable;

public record AuthorDto(
        String id,
        String name
) implements Serializable {
}
