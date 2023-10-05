package ru.otus.andrk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.service.BookService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BookService bookService;

    //@GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/")
    @ResponseBody
    public List<BookDto> getAllBooks(){
        return bookService.getAllBooks();
    }
}
