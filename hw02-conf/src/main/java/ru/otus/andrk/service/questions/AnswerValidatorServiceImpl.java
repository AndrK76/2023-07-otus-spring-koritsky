package ru.otus.andrk.service.questions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.model.QuestionType;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerValidatorServiceImpl implements AnswerValidatorService {

    @Override
    public List<Answer> getAnswersFromString(Question question, String answerText) {
        List<Answer> ret = new ArrayList<>();
        var userNumbers = answerText.split("\\s*,[,\\s]*");
        for (var userNumber : userNumbers) {
            try {
                var answer = getAnswerByNum(question, Integer.parseInt(userNumber));
                if (answer.isValid()) {
                    ret.add(answer);
                }
            } catch (NumberFormatException e) {
                throw new IncorrectAnswerException("Answer contain invalid characters", e);
            }
        }
        return ret;
    }

    private Answer getAnswerByNum(Question question, int num) {
        try {
            return question.getAnswers().get(num - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new IncorrectAnswerException("No exist answer number", e);
        }
    }

    @Override
    public boolean checkAnswer(Question question, List<Answer> answers) {
        long exactSuccessCount = question.getAnswers()
                .stream().filter(Answer::isValid).count();
        long actualSuccessCount = answers.stream().distinct().filter(Answer::isValid).count();

        return question.getQueryType() == QuestionType.ONE_VALID_ANSWER
                ? actualSuccessCount > 0 && answers.size() == 1
                : exactSuccessCount == actualSuccessCount;
    }
}
