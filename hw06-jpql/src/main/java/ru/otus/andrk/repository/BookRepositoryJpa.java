package ru.otus.andrk.repository;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.andrk.model.Book;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@RequiredArgsConstructor
@Repository
public class BookRepositoryJpa implements BookRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            var bookCopy = book.copy();
            em.persist(bookCopy);
            return bookCopy;
        }
        return em.merge(book);
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        setHintsToQuery(query);
        return query.getResultList();
    }

    @Override
    public Optional<Book> findById(long id) {
        TypedQuery<Book> query = em.createQuery("select b from Book b where b.id=:id", Book.class);
        setHintsToQuery(query);
        query.setParameter("id", id);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void delete(Book book) {
        em.remove(book);
    }

    private void setHintsToQuery(TypedQuery<Book> query) {
        EntityGraph<?> entityGraph = em.getEntityGraph("books-detail-entity-graph");
        query.setHint(FETCH.getKey(), entityGraph);
    }
}
