package ru.otus.andrk.exception.converter;

public interface ExceptionToStringMapper {
    String getExceptionMessage(Exception e);
}
