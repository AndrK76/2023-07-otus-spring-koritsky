package ru.otus.andrk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.service.data.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/api/v1/author")
    public List<AuthorDto> getAuthors() {
        return authorService.getAllAuthors();
    }
}
