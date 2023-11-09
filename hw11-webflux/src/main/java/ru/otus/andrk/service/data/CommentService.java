package ru.otus.andrk.service.data;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.andrk.dto.CommentDto;

public interface CommentService {

    Flux<CommentDto> getCommentsByBookId(String bookId);

    Mono<Void> deleteAllCommentsForBook(String bookId);

    Mono<Void> deleteComment(String commentId);

    Mono<CommentDto> addCommentToBook(String bookId, CommentDto comment);

    Mono<CommentDto> modifyComment(String commentId, CommentDto comment);


}
