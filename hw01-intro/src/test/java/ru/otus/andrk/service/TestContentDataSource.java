package ru.otus.andrk.service;

import lombok.Getter;
import lombok.Setter;
import ru.otus.andrk.model.Question;

import java.util.List;

@Getter
@Setter
public class TestContentDataSource {

    private final Question question1 = Question.builder().num(1).build();
    private final Question question2 = Question.builder().num(2).build();

    private final int index = 0;

    public Question getQuestion() {
        return switch (index) {
            case 0 -> question1;
            case 1 -> question2;
            default -> null;
        };
    }

    public List<Question> getQuestions ()
    {
        return List.of(question1,question2);
    }


}
