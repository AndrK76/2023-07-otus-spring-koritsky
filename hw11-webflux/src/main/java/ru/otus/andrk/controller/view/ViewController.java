package ru.otus.andrk.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final Scheduler scheduler;

    @GetMapping("/")
    public Mono<Rendering> index(){
        return Mono.just(Rendering.view("book_list").build()).publishOn(scheduler);
    }

}
