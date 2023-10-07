package ru.otus.andrk.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Genre;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class BookDto implements Serializable {
    private long id;

    private String name;

    private Long authorId;

    private String authorName;

    private Long genreId;

    private String genreName;
}
