package ru.otus.andrk.service.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.andrk.config.LibraryConfig;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.BookWithCommentsDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.repository.BookRepository;
import ru.otus.andrk.repository.CommentRepository;

import java.time.Duration;

@Service
@Log4j2
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepo;

    private final CommentRepository commentRepo;

    private final DtoMapper mapper;
    private final Scheduler scheduler;
    private final LibraryConfig config;

    @Override
    public Flux<BookDto> getAllBooks() {
        log.debug("call get all books");
        return bookRepo.findAll()
                .timeout(Duration.ofMillis(config.getWaitDataInMs()), scheduler)
                .delayElements(Duration.ofMillis(config.getListDelayInMs()), scheduler)
                .map(mapper::toDto)
                .doFirst(() -> log.debug("Start get all books"))
                .doOnNext(book -> log.debug("Get book {}", book.getId()))
                .doOnComplete(() -> log.debug("End get all books"));
    }

    @Override
    public Mono<BookDto> getBookById(long id) {
        return null;
    }

    @Override
    public Mono<BookWithCommentsDto> getBookWithCommentsById(long id) {
        return null;
    }

    @Override
    public Mono<BookDto> addBook(Mono<BookDto> book) {
        return null;
    }

    @Override
    public Mono<BookDto> modifyBook(long bookId, Mono<BookDto> book) {
        return null;
    }

    @Override
    @Transactional
    public Mono<Void> deleteBook(String id) {
        return commentRepo.findByBook_Id(id)
                .publishOn(scheduler)
                .doOnNext(commentRepo::delete)
                .doOnNext(l -> log.debug("delete comment id={}", l.getId()))
                .doOnComplete(() -> bookRepo.deleteById(id))
                .doOnComplete(() -> log.debug("delete book id={}", id))
                .then();
    }
}
