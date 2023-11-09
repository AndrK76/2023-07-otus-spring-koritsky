package ru.otus.andrk.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record CommentDto(
        String id,
        @NotBlank(message = "comment.message-text-not-empty")
        String text
) implements Serializable {
}
