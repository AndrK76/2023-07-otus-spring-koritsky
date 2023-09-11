package ru.otus.andrk.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.model.Comment;

@Service
public class CommentToCommentDtoConverter implements Converter<Comment, CommentDto> {
    @Override
    public CommentDto convert(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }
}
