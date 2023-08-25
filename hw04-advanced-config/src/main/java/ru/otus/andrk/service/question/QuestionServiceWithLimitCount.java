package ru.otus.andrk.service.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.andrk.config.LimitCountConfig;
import ru.otus.andrk.dao.QuestionDao;
import ru.otus.andrk.model.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class QuestionServiceWithLimitCount implements QuestionSourceService {

    private final QuestionDao dao;

    private final LimitCountConfig limitCountConfig;

    @Override
    public List<Question> getQuestions() {
        var allQuestions = dao.getQuestions();
        var needCountQuestions = limitCountConfig.getMaxCountQuestionsPerTest();
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
}
