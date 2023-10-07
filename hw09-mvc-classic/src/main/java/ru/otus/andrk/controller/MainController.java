package ru.otus.andrk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.NoExistBookException;
import ru.otus.andrk.exception.converter.ExceptionToStringMapper;
import ru.otus.andrk.service.BookService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BookService bookService;

    private final ExceptionToStringMapper exceptionMapper;

    @GetMapping("/")
    public String index(Model model) {
        var books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "index";
    }

    @GetMapping("/add")
    public String addBook(Model model) {
        return "edit_book";
    }

    @GetMapping("/edit")
    public String editBook(@RequestParam(name = "id") long idBook, Model model) {
        var book = Optional.ofNullable(bookService.getBookById(idBook)).orElseThrow(NoExistBookException::new);
        model.addAttribute("book", book);
        return "edit_book";
    }

    @ExceptionHandler(KnownLibraryManipulationException.class)
    public String knownError(KnownLibraryManipulationException e, Model model) {
        model.addAttribute("message", exceptionMapper.getExceptionMessage(e));
        return "known_error";
    }

}
