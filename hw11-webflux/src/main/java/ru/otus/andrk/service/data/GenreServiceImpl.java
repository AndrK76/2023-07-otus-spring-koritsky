package ru.otus.andrk.service.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.andrk.config.ControllerConfig;
import ru.otus.andrk.config.DataLayerConfig;
import ru.otus.andrk.dto.GenreDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.model.Genre;
import ru.otus.andrk.repository.GenreRepository;

import java.time.Duration;

@Service
@Log4j2
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository repo;

    private final DtoMapper mapper;

    private final DataLayerConfig config;

    @Override
    public Flux<GenreDto> getAllGenres() {
        return repo.findAll()
                .publishOn(config.getScheduler())
                .timeout(Duration.ofMillis(config.getWaitDataInMs()), config.getScheduler())
                .map(mapper::toDto);
    }

    @Override
    @Transactional
    public Mono<GenreDto> addGenre(String genreName) {
        return repo.save(new Genre(genreName)).publishOn(config.getScheduler())
                .map(mapper::toDto)
                .doOnNext(genre->log.debug("Add genre: {}",genre));
    }


    @Override
    public Mono<GenreDto> getGenreByName(String name) {
        return repo.findFirstByName(name).publishOn(config.getScheduler())
                .map(mapper::toDto);
    }
}
