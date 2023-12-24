package ru.otus.andrk.model.sql;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentRdb {
    private long id;

    private String text;

    private String mongoId;

    private Long bookId;

    public CommentRdb(String text, String mongoId, Long bookId) {
        this.text = text;
        this.mongoId = mongoId;
        this.bookId = bookId;
    }
}
