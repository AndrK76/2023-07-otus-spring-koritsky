package ru.otus.andrk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.GenreDto;
import ru.otus.andrk.service.AuthorService;
import ru.otus.andrk.service.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/api/v1/author")
    public List<AuthorDto> getAuthors(){
        return authorService.getAllAuthors();
    }
}
