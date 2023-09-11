package ru.otus.andrk.repository;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryJpa implements CommentRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Comment> findCommentsForBook(Book book) {
        TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.book=:book",
                Comment.class);
        setHintsToQuery(query);
        query.setParameter("book", book);
        return query.getResultList();
    }

    @Override
    public Optional<Comment> findCommentById(long id) {
        TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.id=:id",
                Comment.class);
        query.setParameter("id", id);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            var copy = comment.copy();
            em.persist(copy);
            return copy;
        }
        return em.merge(comment);
    }

    @Override
    public void delete(Comment comment) {
        em.remove(comment);
    }

    private void setHintsToQuery(TypedQuery<Comment> query) {
        EntityGraph<?> entityGraph = em.getEntityGraph("comment-book-entity-graph");
        query.setHint(FETCH.getKey(), entityGraph);
    }
}
