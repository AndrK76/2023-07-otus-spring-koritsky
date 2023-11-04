package ru.otus.andrk.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.andrk.config.ControllerConfig;
import ru.otus.andrk.config.LocalizationConfig;
import ru.otus.andrk.service.i18n.MessageService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class MessageController {
    private final MessageService messageService;

    private final LocalizationConfig config;

    @GetMapping({"/api/v1/message/{lang}", "/api/v1/message"})
    private Mono<Map<String, String>> getMessages(
            @PathVariable(name = "lang", required = false) String lang) {
        return messageService.getMessages(lang == null ? config.getDefaultLang() : lang);
    }


}
