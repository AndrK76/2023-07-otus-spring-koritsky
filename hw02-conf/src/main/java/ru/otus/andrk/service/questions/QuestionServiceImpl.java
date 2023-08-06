package ru.otus.andrk.service.questions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dao.QuestionDao;
import ru.otus.andrk.model.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao dao;

    @Override
    public List<Question> getNoMoreQuestions(int maxQuestions) {
        var allQuestions = dao.getQuestions();
        if (allQuestions.size() <= maxQuestions) {
            return allQuestions;
        } else {
            Map<Integer, Question> ret = new HashMap<>();
            var random = new Random();
            while (ret.size() < maxQuestions) {
                var queryInd = random.nextInt(allQuestions.size());
                ret.put(queryInd, allQuestions.get(queryInd));
            }
            return ret.values().stream().toList();
        }
    }
}
