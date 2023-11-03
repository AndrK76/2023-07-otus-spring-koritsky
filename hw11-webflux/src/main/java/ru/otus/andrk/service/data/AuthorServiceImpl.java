package ru.otus.andrk.service.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.andrk.config.LibraryConfig;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.repository.AuthorRepository;

import java.time.Duration;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repo;

    private final DtoMapper mapper;

    private final Scheduler scheduler;

    private final LibraryConfig config;


    @Override
    public Flux<AuthorDto> getAllAuthors() {
        return repo.findAll()
                .publishOn(scheduler)
                .timeout(Duration.ofMillis(config.getWaitDataInMs()), scheduler)
                .map(mapper::toDto);
    }

    @Override
    @Transactional
    public Mono<AuthorDto> addAuthor(String authorName) {
        return repo.save(new Author(authorName)).publishOn(scheduler)
                .map(mapper::toDto)
                .doOnNext(author->log.debug("Add author: {}",author));
    }


    @Override
    public Mono<AuthorDto> getAuthorByName(String name) {
        return repo.findFirstByName(name).publishOn(scheduler)
                .map(mapper::toDto);
    }
}
