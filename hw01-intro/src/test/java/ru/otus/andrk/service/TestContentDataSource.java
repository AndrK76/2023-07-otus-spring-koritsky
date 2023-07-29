package ru.otus.andrk.service;

import lombok.Getter;
import lombok.Setter;
import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.model.QuestionType;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class TestContentDataSource {

    private final Question question1 = Question.builder()
            .num(1)
            .queryText("Query #1 text")
            .queryType(QuestionType.ONE_VALID_ANSWER)
            .answers(
                    List.of(
                            Answer.builder()
                                    .num(1)
                                    .answerText("Answer 1 text")
                                    .valid(false)
                                    .build(),
                            Answer.builder()
                                    .num(2)
                                    .answerText("Answer 2 text")
                                    .valid(true)
                                    .build()
                    )
            )
            .build();
    private final Question question2 = Question.builder()
            .num(2)
            .queryText("Query #2 text")
            .queryType(QuestionType.MANY_VALID_ANSWERS)
            .answers(
                    List.of(
                            Answer.builder()
                                    .num(1)
                                    .answerText("Answer 1 text")
                                    .valid(false)
                                    .build(),
                            Answer.builder()
                                    .num(2)
                                    .answerText("Answer 2 text")
                                    .valid(true)
                                    .build(),
                            Answer.builder()
                                    .num(3)
                                    .answerText("Answer 3 text")
                                    .valid(true)
                                    .build()
                    )
            )
            .build();

    public List<Question> getQuestions() {
        return List.of(question1, question2);
    }


}
