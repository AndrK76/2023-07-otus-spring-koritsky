package ru.otus.andrk.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class ViewController {

    @GetMapping({"/","/book"})
    public String bookList(Model model) {
        return "book_list";
    }
}
