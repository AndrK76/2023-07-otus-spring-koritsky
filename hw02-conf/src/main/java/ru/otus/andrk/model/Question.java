package ru.otus.andrk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class Question {
    private final String queryText;

    private final QuestionType queryType;

    @Builder.Default
    private final Map<Integer, Answer> answers = new HashMap<>();
}
