package ru.otus.andrk.service.question;

public abstract class AbstractIncorrectAnswerException extends RuntimeException {
    protected AbstractIncorrectAnswerException(String message) {
        super(message);
    }

    protected AbstractIncorrectAnswerException(String message, Throwable cause) {
        super(message, cause);
    }
}
