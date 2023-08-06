package ru.otus.andrk.service.questions;

import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QuestionConverterServiceSimple implements QuestionConverterService {
    @Override
    public String queryToString(Question question) {
        StringBuilder sb = new StringBuilder(question.getQueryText())
                .append("\n")
                .append("Answers:\n");
        question.getAnswers()
                .forEach((index, answer) -> sb
                        .append("\t")
                        .append(index)
                        .append(". ")
                        .append(answer.getAnswerText())
                        .append("\n"));
        sb.append(
                switch (question.getQueryType()) {
                    case ONE_VALID_ANSWER -> "Enter valid number:";
                    case MANY_VALID_ANSWERS -> "Enter all valid numbers via comma:";
                }
        );
        return sb.toString();
    }

    @Override
    public List<Answer> answersFromString(Question question, String userAnswerText) {
        List<Answer> ret = new ArrayList<>();
        var userNumbers = userAnswerText.split("\\s*,[,\\s]*");

        for (var userNumber : userNumbers) {
            try {
                int answerNmb = Integer.parseInt(userNumber);
                ret.add(question.getAnswers().entrySet()
                        .stream()
                        .filter(r -> r.getKey() == answerNmb)
                        .map(Map.Entry::getValue)
                        .findFirst().orElseThrow(() -> new IncorrectAnswerException("No exist answer number")));
            } catch (NumberFormatException e) {
                throw new IncorrectAnswerException("Answer contain invalid characters", e);
            }
        }
        return ret;

    }
}
