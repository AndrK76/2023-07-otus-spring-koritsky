package ru.otus.andrk.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    @GetMapping({"/", "/book"})
    public String bookView(
            @RequestParam(name = "action", defaultValue = "list") String action,
            @RequestParam(name = "id", defaultValue = "") Long bookId,
            Model model) {
        if (action.equals("add")) {
            model.addAttribute("action", "add");
            model.addAttribute("id", "0");
            model.addAttribute("backUrl", "/book");
            model.addAttribute("title", "book.add-title");
            return "book_edit";
        } else if (action.equals("edit")) {
            model.addAttribute("action", "edit");
            model.addAttribute("id", bookId);
            model.addAttribute("backUrl", "/book");
            model.addAttribute("title", "book.edit-title");
            return "book_edit";
        } else if (action.equals("comments")) {
            model.addAttribute("id", bookId);
            model.addAttribute("backUrl", "/book");
            return "comment_list";
        }
        return "book_list";
    }

}
