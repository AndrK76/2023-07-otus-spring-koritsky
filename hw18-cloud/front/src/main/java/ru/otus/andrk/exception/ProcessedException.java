package ru.otus.andrk.exception;

import lombok.Getter;
import ru.otus.andrk.dto.ApiErrorDto;

@Getter
public class ProcessedException extends RuntimeException {
    private final ApiErrorDto data;

    public ProcessedException(ApiErrorDto data) {
        super();
        this.data = data;
    }
}
