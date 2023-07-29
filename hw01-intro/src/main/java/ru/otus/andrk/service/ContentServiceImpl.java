package ru.otus.andrk.service;

import ru.otus.andrk.dao.ContentDao;
import ru.otus.andrk.model.Question;

import java.util.List;

public class ContentServiceImpl implements ContentService {
    private final ContentDao dao;
    private List<Question> questions;
    private Integer currentQuestionIndex;

    public ContentServiceImpl(ContentDao dao) {
        this.dao = dao;
    }

    @Override
    public int countQueries() {
        initQuestions();
        return questions.size();
    }

    @Override
    public Question getNextQuestion() {
        currentQuestionIndex = currentQuestionIndex == null ? 0 : ++currentQuestionIndex;
        if (currentQuestionIndex < countQueries()) {
            return questions.get(currentQuestionIndex);
        } else {
            return null;
        }
    }

    @Override
    public void reset() {
        currentQuestionIndex = null;
    }

    private void initQuestions() {
        if (questions == null) {
            questions = dao.getQuestions();
        }
    }
}
