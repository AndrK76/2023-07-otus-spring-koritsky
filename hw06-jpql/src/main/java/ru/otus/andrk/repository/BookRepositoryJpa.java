package ru.otus.andrk.repository;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.andrk.model.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@RequiredArgsConstructor
@Repository
public class BookRepositoryJpa implements BookRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public Book save(Book book) {
        return em.merge(book);
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        query.setHint(FETCH.getKey(), getGraphForQueryWithAuthorAndGenre());
        return query.getResultList();
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(
                em.find(Book.class, id, Map.of(FETCH.getKey(), getGraphForQueryWithAuthorAndGenre())));
    }

    @Override
    public void delete(Book book) {
        em.remove(book);
    }

    private EntityGraph<?> getGraphForQueryWithAuthorAndGenre() {
        return em.getEntityGraph("books-detail-entity-graph");
    }

}
