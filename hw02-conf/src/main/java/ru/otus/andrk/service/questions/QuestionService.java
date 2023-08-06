package ru.otus.andrk.service.questions;

import ru.otus.andrk.model.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getNoMoreQuestions(int maxQuestions);
}
