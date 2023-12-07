package ru.otus.andrk.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@Data
public class BookDto implements Serializable {
    private long id;

    @NotBlank(message = "book.message-name-not-empty")
    private String name;

    private Long authorId;

    private String authorName;

    private Long genreId;

    private String genreName;
}
