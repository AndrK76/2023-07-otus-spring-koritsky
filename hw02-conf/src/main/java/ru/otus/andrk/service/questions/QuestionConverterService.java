package ru.otus.andrk.service.questions;

import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;

import java.util.List;

public interface QuestionConverterService {

    String queryToString(Question question);

    List<Answer> answersFromString(Question question, String userAnswerText);

}
