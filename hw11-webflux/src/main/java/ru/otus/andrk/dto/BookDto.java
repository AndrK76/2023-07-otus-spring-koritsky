package ru.otus.andrk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class BookDto implements Serializable {
    private String id;

    @NotBlank(message = "book.message-name-not-empty")
    private String name;

    private String authorId;

    private String authorName;

    private String genreId;

    private String genreName;
}
