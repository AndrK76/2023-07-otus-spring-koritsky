package ru.otus.andrk.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.NoExistBookException;
import ru.otus.andrk.exception.converter.ExceptionToStringMapper;
import ru.otus.andrk.service.AuthorService;
import ru.otus.andrk.service.BookService;
import ru.otus.andrk.service.GenreService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final ExceptionToStringMapper exceptionMapper;

    @GetMapping("/")
    public String index(Model model) {
        var books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "index";
    }

    @GetMapping("/add")
    public String addBook(Model model) {
        addBookDataToModel(model, "add", new BookDto());
        return "edit_book";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") BookDto book,
                          BindingResult bindingResult,
                          Model model) {
        return applyBook("add", book, bindingResult, model);
    }


    @GetMapping("/edit")
    public String editBook(@RequestParam(name = "id") long idBook, Model model) {
        var book = Optional.ofNullable(bookService.getBookById(idBook))
                .orElseThrow(NoExistBookException::new);
        addBookDataToModel(model, "edit", book);
        return "edit_book";
    }

    @PostMapping("/edit")
    public String editBook(@Valid @ModelAttribute("book") BookDto book,
                           BindingResult bindingResult,
                           Model model) {
        return applyBook("add", book, bindingResult, model);
    }

    @ExceptionHandler(KnownLibraryManipulationException.class)
    public String knownError(KnownLibraryManipulationException e, Model model) {
        model.addAttribute("message", exceptionMapper.getExceptionMessage(e));
        return "known_error";
    }



    public String applyBook(String action,
                            BookDto book, BindingResult bindingResult, Model model) {
        log.debug(book);
        if (bindingResult.hasErrors()) {
            addBookDataToModel(model, action, book);
            return "edit_book";
        }
        return "redirect:/";
    }



    private void addBookDataToModel(Model model, String action, BookDto book) {
        model.addAttribute("action", action);
        model.addAttribute("title", "book." + action + "-title");
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("genres", genreService.getAllGenres());
    }


}
