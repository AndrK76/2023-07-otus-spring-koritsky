package ru.otus.andrk.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Genre;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BookDto implements Serializable {
    private long id;

    private String name;

    private Author author;

    private Genre genre;

    private List<CommentDto> comments;
}
