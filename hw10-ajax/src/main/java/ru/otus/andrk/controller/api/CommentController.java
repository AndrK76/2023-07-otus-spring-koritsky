package ru.otus.andrk.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.BookWithCommentsDto;
import ru.otus.andrk.service.data.CommentService;

@RestController
@Log4j2
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;


}
