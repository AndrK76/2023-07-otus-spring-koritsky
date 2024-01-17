package ru.otus.andrk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.andrk.config.ApiServerConfig;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MainController {
    private final ApiServerConfig apiServerConfig;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("apiServerSettings", apiServerConfig);
        return "index";
    }
}
