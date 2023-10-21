package ru.otus.andrk.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ViewController {

    @GetMapping({"/","/book"})
    public String bookList(Model model) {
        return "book_list";
    }

    @GetMapping("/book/new")
    public String addBook(Model model) {
        model.addAttribute("action", "add");
        model.addAttribute("id", "0");
        model.addAttribute("backUrl", "/book");
        model.addAttribute("title", "book.add-title");
        return "book_edit";
    }

    @GetMapping("/book/edit/{id}")
    public String editBook(@PathVariable(name = "id") int bookId, Model model){
        model.addAttribute("action", "edit");
        model.addAttribute("id", bookId);
        model.addAttribute("backUrl", "/book");
        model.addAttribute("title", "book.edit-title");
        return "book_edit";
    }
}
