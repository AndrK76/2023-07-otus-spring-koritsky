package ru.otus.andrk.service;

import ru.otus.andrk.dao.TestContentDao;
import ru.otus.andrk.model.TestContent;

public class TestContentServiceImpl implements TestContentService {
    private final TestContentDao dao;

    public TestContentServiceImpl(TestContentDao dao) {
        this.dao = dao;
    }

    @Override
    public TestContent getTestContent() {
        return null;
    }
}
