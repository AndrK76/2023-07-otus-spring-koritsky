package ru.otus.andrk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class Question {
    private final String queryText;

    private final QuestionType queryType;

    @Builder.Default
    private final List<Answer> answers = new ArrayList<>();
}
