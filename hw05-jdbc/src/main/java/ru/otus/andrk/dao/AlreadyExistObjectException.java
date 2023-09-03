package ru.otus.andrk.dao;

public class AlreadyExistObjectException extends RuntimeException{
    public AlreadyExistObjectException(Throwable cause) {
        super(cause);
    }
}
