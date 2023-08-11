package ru.otus.andrk.service.questions;

import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestionsForTest();

    List<Answer> getAnswersFromString(Question question, String answerText);

    boolean checkAnswer(Question question, List<Answer> answers);

}
