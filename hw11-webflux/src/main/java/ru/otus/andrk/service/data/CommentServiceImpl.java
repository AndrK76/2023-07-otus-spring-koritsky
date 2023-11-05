package ru.otus.andrk.service.data;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.andrk.config.DataLayerConfig;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.exception.NoExistBookException;
import ru.otus.andrk.exception.NoExistCommentException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.repository.CommentRepository;

import java.time.Duration;
import java.util.function.Function;

@Service
@Log4j2
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repo;

    private final DtoMapper mapper;

    private final DataLayerConfig config;

    private final BookService bookService;

    public CommentServiceImpl(
            CommentRepository repo,
            DtoMapper mapper,
            DataLayerConfig config,
            @Lazy BookService bookService) {
        this.repo = repo;
        this.mapper = mapper;
        this.config = config;
        this.bookService = bookService;
    }

    @Override
    public Flux<CommentDto> getCommentsByBookId(String bookId) {
        return repo.findByBook_Id(bookId)
                .publishOn(config.getScheduler())
                .onErrorMap(OtherLibraryManipulationException::new)
                .timeout(Duration.ofMillis(config.getWaitDataInMs()), config.getScheduler())
                .map(mapper::toDto)
                .doOnNext(comment -> log.debug("Get comment {}", comment.id()));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Mono<Void> deleteAllCommentsForBook(String bookId) {
        return Mono.just(bookId).publishOn(config.getScheduler())
                .onErrorMap(OtherLibraryManipulationException::new)
                .flatMapMany(repo::findByBook_Id)
                .map(Comment::getId)
                .collectList()
                .doOnNext(l -> log.debug("delete comments: {}", l))
                .map(repo::deleteAllById)
                .flatMap(Function.identity());
    }

    @Override
    @Transactional
    public Mono<Void> deleteComment(String commentId) {
        return Mono.just(commentId)
                .doOnError(OtherLibraryManipulationException::new)
                .publishOn(config.getScheduler())
                .doOnNext(c -> log.debug("delete comment id={}", c))
                .flatMap(repo::deleteById);
    }

    @Override
    public Mono<CommentDto> addCommentToBook(String bookId, CommentDto comment) {
        return Mono.just(bookId).publishOn(config.getScheduler())
                .flatMap(bookService::getBook)
                .switchIfEmpty(Mono.error(new NoExistBookException()))
                .publishOn(config.getScheduler())
                .map(b -> new Comment(comment.text(), b))
                .onErrorMap(OtherLibraryManipulationException::new)
                .flatMap(repo::save)
                .doOnNext(l -> log.debug("add comment id={}", l.getId()))
                .map(mapper::toDto);
    }

    @Override
    public Mono<CommentDto> modifyComment(String commentId, CommentDto comment) {
        return Mono.just(commentId).publishOn(config.getScheduler())
                .onErrorMap(OtherLibraryManipulationException::new)
                .flatMap(repo::findById)
                .switchIfEmpty(Mono.error(new NoExistCommentException()))
                .doOnNext(r -> r.setText(comment.text()))
                .flatMap(repo::save)
                .doOnNext(l -> log.debug("modify comment id={}", l.getId()))
                .map(mapper::toDto);

    }
}
