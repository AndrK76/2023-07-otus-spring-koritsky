package ru.otus.andrk.service;

import ru.otus.andrk.dao.ContentDao;
import ru.otus.andrk.model.Question;

public class ContentServiceImpl implements ContentService {
    private final ContentDao dao;

    public ContentServiceImpl(ContentDao dao) {
        this.dao = dao;
    }


    @Override
    public int countQueries() {
        return 0;
    }

    @Override
    public Question getNextQuestion() {
        return null;
    }
}
