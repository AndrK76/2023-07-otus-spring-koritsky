package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repo;

    private final DtoMapper mapper;


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
    public CommentDto addCommentToBook(long bookId, CommentDto comment) {
        return null;
    }

    @Override
    public CommentDto modifyComment(long commentId, CommentDto comment) {
        return null;
    }

    @Override
    public void deleteComment(long id) {

    }
}
