package ru.otus.andrk.dto.mapper;

import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.MessagePair;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;

import java.util.Map;

public interface ApiErrorMapper {
    ApiErrorDto fromErrorAttributes(Map<String, Object> errAttrs);

    ApiErrorDto fromOtherError(OtherLibraryManipulationException e);

    ApiErrorDto fromKnownError(KnownLibraryManipulationException e);

    Map<String, MessagePair> fromNotValidArgument(MethodArgumentNotValidException e);
}
