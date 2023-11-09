package ru.otus.andrk.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class ApiErrorDto implements Serializable {
    private final Date timestamp;

    private final int status;

    private MessagePair statusMessage;

    private MessagePair errorMessage;

    private MessagePair detailMessage;

    private Serializable details;
}
