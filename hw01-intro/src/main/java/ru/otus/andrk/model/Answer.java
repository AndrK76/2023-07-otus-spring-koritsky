package ru.otus.andrk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Answer {
    private int num;
    private String answerText;
    private Boolean valid;
}
