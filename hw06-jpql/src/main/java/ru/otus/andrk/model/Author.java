package ru.otus.andrk.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.andrk.interfaces.Copyable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authors")
@Entity
public class Author implements Copyable<Author> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Override
    public Author copy() {
        return new Author(this.id, this.name);
    }
}
