package ru.otus.andrk.model.nosql;

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
public class CommentMongo {
    @Id
    private String id;

    @Setter
    private String text;

    @DBRef
    @ToString.Exclude
    private BookMongo book;

    public CommentMongo(String text, BookMongo book) {
        this.text = text;
        this.book = book;
    }
}
