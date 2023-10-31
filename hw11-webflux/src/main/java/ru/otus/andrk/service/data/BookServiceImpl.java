package ru.otus.andrk.service.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.andrk.config.LibraryConfig;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.BookWithCommentsDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.repository.BookRepository;

import java.time.Duration;

@Service
@Log4j2
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepo;
    private final DtoMapper mapper;
    private final Scheduler scheduler;
    private final LibraryConfig config;

    @Override
    public Flux<BookDto> getAllBooks() {
        log.debug("call get all books");
        return bookRepo.findAll()
                .timeout(Duration.ofMillis(500), scheduler)
                .delayElements(Duration.ofMillis(config.getListDelayInMs()),scheduler)
                .map(mapper::toDto)
                .doFirst(()->log.debug("Start get all books"))
                .doOnNext(book->log.debug("Get book {}",book.getId()))
                .doOnComplete(()->log.debug("End get all books"));
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
    public void deleteBook(long id) {

    }
}
