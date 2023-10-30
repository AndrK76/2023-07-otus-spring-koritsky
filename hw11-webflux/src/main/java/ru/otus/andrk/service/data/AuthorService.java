package ru.otus.andrk.service.data;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.andrk.dto.AuthorDto;

public interface AuthorService {
    Flux<AuthorDto> getAllAuthors();

    Mono<AuthorDto> addAuthor(String authorName);

    Mono<AuthorDto> getAuthorById(long authorId);

    Mono<AuthorDto> getAuthorByName(String name);
}
