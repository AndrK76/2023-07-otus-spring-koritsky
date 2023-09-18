package ru.otus.andrk.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryJpa implements CommentRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Comment> findCommentsForBook(Book book) {

        TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.book=:book",
                Comment.class);
        query.setParameter("book", book);
        return query.getResultList();
    }

    @Override
    public Optional<Comment> findCommentById(long id) {
        return Optional.ofNullable(em.find(Comment.class,id));
    }

    @Override
    public Comment save(Comment comment) {
        return em.merge(comment);
    }

    @Override
    public void delete(Comment comment) {
        em.remove(comment);
    }

}
