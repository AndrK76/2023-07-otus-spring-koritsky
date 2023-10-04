package ru.otus.andrk.dto;

import java.io.Serializable;

public record CommentDto(
        long id,
        String text
) implements Serializable {
}
