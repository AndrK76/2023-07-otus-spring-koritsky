package ru.otus.andrk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "authors")
public class Author {
    @Transient
    public static final String SEQUENCE_NAME = "authors_seq";

    @Id
    private long id;

    private String name;

    public Author(String name) {
        this.name = name;
    }
}
