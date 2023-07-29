package ru.otus.andrk.service;

import ru.otus.andrk.model.Question;

import java.util.Collection;


public interface ContentService {

    Collection<Question> getQuestions();

}
