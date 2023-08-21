package ru.otus.andrk.service.question;

import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;

import java.util.List;

public interface AnswerValidatorService {
    List<Answer> getAnswersFromString(Question question, String answerText);

    boolean checkAnswer(Question question, List<Answer> answers);

}
