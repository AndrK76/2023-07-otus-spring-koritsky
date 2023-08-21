package ru.otus.andrk.service.question;

import ru.otus.andrk.model.Question;

import java.util.List;

public interface QuestionSourceService {
    List<Question> getQuestions();
}
