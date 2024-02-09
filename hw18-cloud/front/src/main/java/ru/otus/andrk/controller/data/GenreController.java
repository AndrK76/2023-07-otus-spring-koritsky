package ru.otus.andrk.controller.data;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.service.library.GenreService;

@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/api/v1/genre")
    public ResponseEntity<?> getGenres() {
        return genreService.getGenres();
    }
}
