package ru.otus.andrk.service.main;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.dto.CommentOnBookDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.NoExistBookException;
import ru.otus.andrk.exception.NoExistCommentException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.repository.BookRepository;
import ru.otus.andrk.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repo;

    private final BookRepository bookRepo;

    private final DtoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CommentOnBookDto> getCommentsForBook(long bookId) {
        try {
            var book = bookRepo.findById(bookId);
            if (book.isEmpty()) {
                throw new NoExistBookException();
            }
            return repo.findCommentsByBook(book.get()).stream()
                    .map(mapper::toDtoWithBook).toList();
        } catch (KnownLibraryManipulationException e) {
            log.error(e);
            throw e;
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CommentOnBookDto getCommentById(long id) {
        try {
            var comment = repo.findById(id).orElse(null);
            if (comment != null) {
                var book = bookRepo.findById(comment.getBook().getId());
                return mapper.toDtoWithBook(comment);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    @Transactional
    public CommentOnBookDto addCommentForBook(long bookId, String text) {
        Comment comment;
        try {
            var book = bookRepo.findById(bookId);
            if (book.isEmpty()) {
                throw new NoExistBookException();
            }
            comment = new Comment();
            comment.setBook(book.get());
            comment.setText(text);
        } catch (KnownLibraryManipulationException e) {
            log.error(e);
            throw e;
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
        return saveComment(comment);
    }

    @Override
    @Transactional
    public CommentOnBookDto modifyComment(long commentId, String newText) {
        Comment comment;
        try {
            comment = repo.findById(commentId).orElse(null);
            if (comment == null) {
                throw new NoExistCommentException();
            }
            comment.setBook(bookRepo.findById(comment.getBook().getId()).get());
            comment.setText(newText);
        } catch (KnownLibraryManipulationException e) {
            log.error(e);
            throw e;
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
        return saveComment(comment);
    }

    @Override
    @Transactional
    public void deleteComment(long id) {
        var comment = repo.findById(id);
        comment.ifPresent(repo::delete);
    }

    private CommentOnBookDto saveComment(Comment comment) {
        try {
            var savedComment = Optional.of(repo.save(comment));
            return savedComment.map(mapper::toDtoWithBook)
                    .orElse(null);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }
}
