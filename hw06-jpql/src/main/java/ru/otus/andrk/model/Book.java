package ru.otus.andrk.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@NamedEntityGraph(name = "books-detail-entity-graph",
        attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("genre")})

@Getter
@Setter
@NoArgsConstructor
@Table(name = "books")
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.PRIVATE)
    private long id = 0;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = {CascadeType.ALL})
    @BatchSize(size = 50)
    private List<Comment> comments;


    public Book(String name) {
        this.name = name;
    }

    public Book(String name, Author author, Genre genre) {
        this.name = name;
        this.author = author;
        this.genre = genre;
    }

    @Override
    public String toString() {
        String commentsStr = "null";
        if (comments != null) {
            commentsStr = "[size=" + comments.size() + "]";
        }
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author=" + author +
                ", genre=" + genre +
                ", comments=" + commentsStr +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return equalWithBook((Book) o);
    }

    private boolean equalWithBook(Book book) {
        if (getId() != book.getId()) {
            return false;
        }
        if (getName() != null ? !getName().equals(book.getName()) : book.getName() != null) {
            return false;
        }
        if (getAuthor() != null ? getAuthor().getId() != book.getAuthor().getId() : book.getAuthor() != null) {
            return false;
        }
        if (getGenre() != null ? getGenre().getId() != book.getGenre().getId() : book.getGenre() != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getAuthor() != null ? ((Long) (getAuthor().getId())).hashCode() : 0);
        result = 31 * result + (getGenre() != null ? ((Long) (getGenre().getId())).hashCode() : 0);
        return result;
    }
}
