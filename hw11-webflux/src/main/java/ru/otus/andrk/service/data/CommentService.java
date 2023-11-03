package ru.otus.andrk.service.data;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.andrk.dto.CommentDto;

public interface CommentService {

    Flux<CommentDto> getCommentsByBookId(String bookId);

    Mono<Void> deleteAllCommentsForBook(String bookId);


}
