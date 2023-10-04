package ru.otus.andrk.dto;

import java.io.Serializable;

public record CommentOnBookDto(
        long id,
        String text,
        long bookId,
        String bookName,
        String authorName,
        String genreName
) implements Serializable {
}
