package ru.otus.andrk.service.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.NoExistBookException;
import ru.otus.andrk.exception.NoExistCommentException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.repository.BookRepository;
import ru.otus.andrk.repository.CommentRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repo;

    private final BookRepository bookRepo;

    private final DtoMapper mapper;

    private final BuBuService buBuService;


    @Override
    public CommentDto getCommentById(long id) {
        log.debug("get {}", id);
        Optional<Comment> comment;
        try {
            buBuService.tryBuBu();
            comment = repo.findById(id);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
        return comment.map(mapper::toDto).orElseThrow(NoExistCommentException::new);
    }

    @Override
    @Transactional
    public CommentDto addCommentForBook(long bookId, String text) {
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
    public CommentDto modifyComment(long commentId, String newText) {
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
        try {
            var comment = repo.findById(id);
            comment.ifPresent(repo::delete);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    private CommentDto saveComment(Comment comment) {
        try {
            buBuService.tryBuBu();
            var savedComment = Optional.of(repo.save(comment));
            return savedComment.map(mapper::toDto)
                    .orElse(null);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }
}
