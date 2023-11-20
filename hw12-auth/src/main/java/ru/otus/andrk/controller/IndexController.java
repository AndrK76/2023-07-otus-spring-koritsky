package ru.otus.andrk.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@Log4j2
public class IndexController {

    @GetMapping({"/", "/index"})
    public String index(Model model, Principal user) {
        if (user != null && ((Authentication) user).isAuthenticated()) {
            return "redirect:/book";
        }
        return "index";
    }
}
