package ru.otus.andrk.service.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.andrk.config.ControllerConfig;
import ru.otus.andrk.config.DataLayerConfig;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.repository.CommentRepository;

import java.time.Duration;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repo;

    private final DtoMapper mapper;

    private final DataLayerConfig config;

    @Override
    public Flux<CommentDto> getCommentsByBookId(String bookId) {
        return repo.findByBook_Id(bookId)
                .onErrorMap(OtherLibraryManipulationException::new)
                .timeout(Duration.ofMillis(config.getWaitDataInMs()), config.getScheduler())
                .map(mapper::toDto)
                .doOnNext(comment -> log.debug("Get comment {}", comment.id()));
    }

    @Override
    public Mono<Void> deleteAllCommentsForBook(String bookId) {
        return getCommentsByBookId(bookId)
                .publishOn(config.getScheduler())
                .map(CommentDto::id)
                .collectList()
                .doOnNext(l -> log.debug("delete comments ids={} size={}", l, l.size()))
                .doOnNext(repo::deleteAllById)
                .then();
    }
}
