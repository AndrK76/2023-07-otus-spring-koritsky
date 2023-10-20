package ru.otus.andrk.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class ApiErrorDto {
    private final Date timestamp;
    private final int status;
    private String statusMessage;
    private String statusMessageKey;
    private String errorMessage;
    private String errorMessageKey;
    private String detailMessage;
    private String detailMessageKey;
}
