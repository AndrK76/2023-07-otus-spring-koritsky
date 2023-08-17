package ru.otus.andrk.service.question;

public class IncorrectAnswerCharactersException extends AbstractIncorrectAnswerException {
    public IncorrectAnswerCharactersException(String message) {
        super(message);
    }

    public IncorrectAnswerCharactersException(String message, Throwable cause) {
        super(message, cause);
    }

}
