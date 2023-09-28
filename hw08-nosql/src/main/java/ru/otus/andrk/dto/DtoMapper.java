package ru.otus.andrk.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.model.Book;

@Component
@RequiredArgsConstructor
public class DtoMapper {
    //private final CommentToCommentDtoConverter commentConverter;

    public BookDto bookToDto(Book book) {
        var ret = BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .build();
//        if (book.getComments() != null) {
//            ret.setComments(book.getComments().stream()
//                    .map(commentConverter::convert).toList());
//        }
        return ret;
    }
}
