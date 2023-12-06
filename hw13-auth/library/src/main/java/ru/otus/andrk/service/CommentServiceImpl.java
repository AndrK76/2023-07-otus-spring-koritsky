package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.NoExistBookException;
import ru.otus.andrk.exception.NoExistCommentException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repo;

    private final DtoMapper mapper;

    private final BookService bookService;


    @Override
    public List<CommentDto> getCommentsForBook(long bookId) {
        try {
            return repo.findCommentsByBook_Id(bookId).stream()
                    .map(mapper::toDto).toList();
        } catch (Exception ex) {
            throw new OtherLibraryManipulationException(ex);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole(@roleConfig.getRolesForAction('modify_comment'))")
    public CommentDto addCommentToBook(long bookId, CommentDto dto) {
        var book = bookService.getBookById(bookId)
                .orElseThrow(NoExistBookException::new);
        try {
            var comment = new Comment(book, dto.text());
            return mapper.toDto(repo.save(comment));
        } catch (Exception ex) {
            throw new OtherLibraryManipulationException(ex);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole(@roleConfig.getRolesForAction('modify_comment'))")
    public CommentDto modifyComment(long commentId, CommentDto dto) {
        try {
            var comment = repo.findById(commentId)
                    .orElseThrow(NoExistCommentException::new);
            comment.setText(dto.text());
            return mapper.toDto(repo.save(comment));
        } catch (KnownLibraryManipulationException | OtherLibraryManipulationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new OtherLibraryManipulationException(ex);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole(@roleConfig.getRolesForAction('modify_comment'))")
    public void deleteComment(long id) {
        repo.deleteById(id);
    }
}
