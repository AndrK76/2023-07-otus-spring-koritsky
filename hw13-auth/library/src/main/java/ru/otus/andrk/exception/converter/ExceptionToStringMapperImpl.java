package ru.otus.andrk.exception.converter;

import org.springframework.stereotype.Service;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.NoExistAuthorException;
import ru.otus.andrk.exception.NoExistBookException;
import ru.otus.andrk.exception.NoExistCommentException;
import ru.otus.andrk.exception.NoExistGenreException;

import java.util.Map;
import java.util.Optional;

@Service
public class ExceptionToStringMapperImpl implements ExceptionToStringMapper {

    private final Map<Class, String> messages = Map.of(
            NoExistBookException.class, "book-no-exist",
            NoExistAuthorException.class, "author-no-exist",
            NoExistGenreException.class, "genre-no-exist",
            NoExistCommentException.class, "comment-no-exist"
    );

    @Override
    public String getExceptionMessage(Exception e) {
        if (e instanceof KnownLibraryManipulationException) {
            return "known-error." +
                    Optional.ofNullable(messages.get(e.getClass()))
                            .orElse("other-manipulation-error");
        }
        return "other-error";
    }
}
