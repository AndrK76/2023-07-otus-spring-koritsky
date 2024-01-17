package ru.otus.andrk.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.config.I18nConfig;
import ru.otus.andrk.service.i18n.MessageService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Log4j2
public class I18nController {

    private final MessageService messageService;

    private final I18nConfig i18nConfig;

    @GetMapping({"/api/v1/message/{lang}", "/api/v1/message"})
    private Map<String, String> getMessages(
            @PathVariable(name = "lang", required = false) Optional<String> lang) {
        return messageService.getMessages(lang.orElse(i18nConfig.getDefaultLang()));
    }
}
