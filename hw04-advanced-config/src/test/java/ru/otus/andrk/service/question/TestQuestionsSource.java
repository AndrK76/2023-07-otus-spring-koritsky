package ru.otus.andrk.service.question;

import lombok.Getter;
import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.model.QuestionType;

import java.util.List;

@Getter
public class TestQuestionsSource {
    private final Question questionOneValidTypeWithOneValidAnswer = Question.builder()
            .queryText("Query text")
            .queryType(QuestionType.ONE_VALID_ANSWER)
            .answers(
                    List.of(
                            new Answer("Answer 1 text", true),
                            new Answer("Answer 2 text", false)
                    ))
            .build();
    private final Question questionManyValidTypeWithTwoValidAnswer = Question.builder()
            .queryText("Query text")
            .queryType(QuestionType.MANY_VALID_ANSWERS)
            .answers(
                    List.of(
                            new Answer("Answer 1 text", true),
                            new Answer("Answer 2 text", false),
                            new Answer("Answer 2 text", true)
                    ))
            .build();
    private final Question questionOneValidTypeWithTwoValidAnswer = Question.builder()
            .queryText("Query text")
            .queryType(QuestionType.ONE_VALID_ANSWER)
            .answers(
                    List.of(
                            new Answer("Answer 1 text", true),
                            new Answer("Answer 2 text", false),
                            new Answer("Answer 2 text", true)
                    ))
            .build();

}
