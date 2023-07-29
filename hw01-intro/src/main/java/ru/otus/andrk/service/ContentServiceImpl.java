package ru.otus.andrk.service;

import ru.otus.andrk.dao.ContentDao;
import ru.otus.andrk.model.Question;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ContentServiceImpl implements ContentService {
    private final ContentDao dao;

    private List<Question> questions;

    public ContentServiceImpl(ContentDao dao) {
        this.dao = dao;
    }


    @Override
    public Collection<Question> getQuestions() {
        initQuestions();
        return Collections.unmodifiableCollection(questions);
    }

    private void initQuestions() {
        if (questions == null) {
            questions = dao.getQuestions();
        }
    }
}
