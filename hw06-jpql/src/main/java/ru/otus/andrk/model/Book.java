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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import ru.otus.andrk.interfaces.Copyable;

import java.util.List;
import java.util.stream.Collectors;

@NamedEntityGraph(name = "books-detail-entity-graph",
        attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("genre")})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "books")
@Entity
public class Book implements Copyable<Book> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.PRIVATE)
    @Builder.Default
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

    @Override
    public Book copy() {
        var ret = builder()
                .id(this.id)
                .name(this.name)
                .author(this.author == null ? null : this.author.copy())
                .genre(this.genre == null ? null : this.genre.copy())
                .build();
        if (this.comments != null) {
            ret.comments = this.comments.stream()
                    .map(c -> {
                        var comm = c.copy();
                        comm.setBook(ret);
                        return comm;
                    }).collect(Collectors.toList());
        }
        return ret;
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
        if (getAuthor() != null ? !getAuthor().equals(book.getAuthor()) : book.getAuthor() != null) {
            return false;
        }
        return getGenre() != null ? getGenre().equals(book.getGenre()) : book.getGenre() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getAuthor() != null ? getAuthor().hashCode() : 0);
        result = 31 * result + (getGenre() != null ? getGenre().hashCode() : 0);
        return result;
    }
}
