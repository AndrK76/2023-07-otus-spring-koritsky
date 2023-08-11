package ru.otus.andrk.service.questions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.andrk.config.TestSystemConfig;
import ru.otus.andrk.dao.QuestionDao;
import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.model.QuestionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao dao;

    private final TestSystemConfig testSystemConfig;

    @Override
    public List<Question> getQuestionsForTest() {
        var allQuestions = dao.getQuestions();
        var needCountQuestions = testSystemConfig.getMaxCountQuestionsPerTest();
        if (allQuestions.size() <= needCountQuestions) {
            return allQuestions;
        } else {
            Map<Integer, Question> ret = new HashMap<>();
            var random = new Random();
            while (ret.size() < needCountQuestions) {
                var queryInd = random.nextInt(allQuestions.size());
                ret.put(queryInd, allQuestions.get(queryInd));
            }
            return ret.values().stream().toList();
        }
    }

    @Override
    public List<Answer> getAnswersFromString(Question question, String answerText) {
        List<Answer> ret = new ArrayList<>();
        var userNumbers = answerText.split("\\s*,[,\\s]*");

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
