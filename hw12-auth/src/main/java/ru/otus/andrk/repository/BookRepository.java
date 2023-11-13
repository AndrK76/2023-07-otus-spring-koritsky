package ru.otus.andrk.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.andrk.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    @EntityGraph("books-detail-entity-graph")
    List<Book> findAll();

    @Override
    @EntityGraph("books-detail-entity-graph")
    Optional<Book> findById(Long aLong);
}
