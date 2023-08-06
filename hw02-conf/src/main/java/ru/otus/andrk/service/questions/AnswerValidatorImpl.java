package ru.otus.andrk.service.questions;

import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.model.QuestionType;

import java.util.List;

@Service
public class AnswerValidatorImpl implements AnswerValidator {
    @Override
    public boolean checkAnswer(Question question, List<Answer> answers) {
        long exactSuccessCount = question.getAnswers().values()
                .stream().filter(Answer::isValid).count();
        long actualSuccessCount = answers.stream().distinct().filter(Answer::isValid).count();

        return question.getQueryType() == QuestionType.ONE_VALID_ANSWER
                ? actualSuccessCount > 0 && answers.size() == 1
                : exactSuccessCount == actualSuccessCount;
    }
}
