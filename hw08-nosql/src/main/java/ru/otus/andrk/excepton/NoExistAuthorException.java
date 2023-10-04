package ru.otus.andrk.excepton;

public class NoExistAuthorException extends KnownLibraryManipulationException {
    public NoExistAuthorException(Throwable cause) {
        super(cause);
    }

    public NoExistAuthorException() {
        super();
    }
}
