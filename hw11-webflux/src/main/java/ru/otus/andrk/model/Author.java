package ru.otus.andrk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "authors")
public class Author {


    @Id
    private String id;

    private String name;

    public Author(String name) {
        this.name = name;
    }
}
