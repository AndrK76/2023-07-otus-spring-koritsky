package ru.otus.andrk.service.questions;

import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;

import java.util.List;

public interface AnswerValidator {
    boolean checkAnswer(Question question, List<Answer> answers);
}
