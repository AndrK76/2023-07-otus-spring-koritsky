package ru.otus.andrk.dao;

import ru.otus.andrk.model.Question;

import java.util.List;

public interface ContentDao {
    List<Question> getQuestions();
}
