package ru.otus.andrk.controller.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MainController {


    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
}
