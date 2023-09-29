package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.dto.CommentOnBookDto;
import ru.otus.andrk.dto.DtoMapper;
import ru.otus.andrk.excepton.KnownLibraryManipulationException;
import ru.otus.andrk.excepton.NoExistBookException;
import ru.otus.andrk.excepton.NoExistCommentException;
import ru.otus.andrk.excepton.OtherLibraryManipulationException;
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
                    .map(mapper::commentToCommentOnBookDto).toList();
        } catch (KnownLibraryManipulationException e) {
            log.error(e);
            throw e;
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    public CommentOnBookDto getCommentById(long id) {
        try {
            return repo.findById(id)
                    .map(mapper::commentToCommentOnBookDto)
                    .orElse(null);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    @Transactional
    public CommentOnBookDto addCommentForBook(long bookId, String text) {
        try {
            var comment = bookRepo.findById(bookId)
                    .map(c-> new Comment(text, c))
                    .orElseThrow(NoExistBookException::new);
            return saveComment(comment);
        } catch (KnownLibraryManipulationException e) {
            log.error(e);
            throw e;
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    public CommentOnBookDto modifyComment(long commentId, String newText) {
        try {
            var comment = repo.findById(commentId).orElse(null);
            if (comment == null) {
                throw new NoExistCommentException();
            }
            comment.setText(newText);
            return saveComment(comment);
        } catch (KnownLibraryManipulationException e) {
            log.error(e);
            throw e;
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    public void deleteComment(long id) {
        var comment = repo.findById(id);
        comment.ifPresent(repo::delete);
    }

    private CommentOnBookDto saveComment(Comment comment) {
        var savedComment = Optional.of(
                comment.getId() == 0L
                        ? repo.insertComment(comment)
                        : repo.save(comment));
        return savedComment.map(mapper::commentToCommentOnBookDto)
                .orElse(null);
    }

}
