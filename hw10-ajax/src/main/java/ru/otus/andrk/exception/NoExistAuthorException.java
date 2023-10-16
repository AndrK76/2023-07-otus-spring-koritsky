package ru.otus.andrk.exception;

public class NoExistAuthorException extends KnownLibraryManipulationException {
    public NoExistAuthorException(Throwable cause) {
        super(cause);
    }

    public NoExistAuthorException() {
        super();
    }
}
