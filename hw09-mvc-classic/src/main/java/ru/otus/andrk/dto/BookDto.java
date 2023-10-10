package ru.otus.andrk.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@ToString
public class BookDto implements Serializable {
    private long id;

    @NotBlank(message = "{book.message-name-not-empty}")
    private String name;

    private Long authorId;

    private String authorName;

    private Long genreId;

    private String genreName;
}
