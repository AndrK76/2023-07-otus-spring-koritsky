package ru.otus.andrk.service.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.repository.AuthorRepository;

import java.time.Duration;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repo;
    private final DtoMapper mapper;


    @Override
    public Flux<AuthorDto> getAllAuthors() {
        return repo.findAll()
                .map(mapper::toDto);
    }

    @Override
    public Mono<AuthorDto> addAuthor(String authorName) {
        return null;
    }

    @Override
    public Mono<AuthorDto> getAuthorById(long authorId) {
        return null;
    }

    @Override
    public Mono<AuthorDto> getAuthorByName(String name) {
        return null;
    }
}
