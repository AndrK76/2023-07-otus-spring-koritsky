package ru.otus.andrk.service;

import ru.otus.andrk.model.Question;

public interface ContentService {
    int countQueries();

    Question getNextQuestion();

    void reset();

}
