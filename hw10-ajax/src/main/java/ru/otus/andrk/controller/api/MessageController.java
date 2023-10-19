package ru.otus.andrk.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.service.i18n.MessageService;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@RestController
@Log4j2
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping({"/api/v1/message/{lang}","/api/v1/message"})
    private Map<String,String> getMessages(
            @RequestParam(name = "name", defaultValue = "") List<String> messageKeys,
            @PathVariable(name = "lang", required = false) Optional<String> lang) {
        return messageService.getMessages(Locale.forLanguageTag(lang.orElse("en")), messageKeys);
    }
}
