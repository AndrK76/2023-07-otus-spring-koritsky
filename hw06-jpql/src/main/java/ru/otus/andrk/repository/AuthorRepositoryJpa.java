package ru.otus.andrk.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.andrk.model.Author;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AuthorRepositoryJpa implements AuthorRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public Author save(Author author) {
        var stored = author.copy();
        if (author.getId() == 0) {
            em.persist(stored);
            return stored;
        }
        return em.merge(stored);
    }

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }
}
