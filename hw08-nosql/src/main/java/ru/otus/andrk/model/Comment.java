package ru.otus.andrk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Comment {

    public static final String SEQUENCE_NAME = "comments_seq";

    @Id
    private long id;

    private String text;

    @DBRef
    @ToString.Exclude
    private Book book;

    public Comment(String text, Book book) {
        this.text = text;
        this.book = book;
    }
}
