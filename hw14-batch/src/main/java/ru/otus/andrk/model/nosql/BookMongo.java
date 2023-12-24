package ru.otus.andrk.model.nosql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Document(collection = "books")
@Getter
@ToString
public class BookMongo {
    @Id
    private String id;

    @Setter
    private String name;

    @Setter
    private AuthorMongo author;

    @Setter
    private GenreMongo genre;

    public BookMongo(String name, AuthorMongo author, GenreMongo genre) {
        this.name = name;
        this.author = author;
        this.genre = genre;
    }

    @SuppressWarnings("unused")
    public BookMongo(String id, String name, AuthorMongo author, GenreMongo genre) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.genre = genre;
    }


}
