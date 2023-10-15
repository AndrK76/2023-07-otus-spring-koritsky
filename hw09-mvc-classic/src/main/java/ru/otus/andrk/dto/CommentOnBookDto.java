package ru.otus.andrk.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public final class CommentOnBookDto implements Serializable {
    private long id;

    @NotBlank(message = "{comment.message-text-not-empty}")
    private String text;

    private long bookId;

    private String bookName;

    private String authorName;

    private String genreName;

    public CommentOnBookDto(BookDto book) {
        this.bookId = book.getId();
        this.bookName = book.getName();
        this.authorName = book.getAuthorName();
        this.genreName = book.getGenreName();
    }
}
