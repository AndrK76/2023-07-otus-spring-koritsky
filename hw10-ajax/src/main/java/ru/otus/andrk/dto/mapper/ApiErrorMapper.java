package ru.otus.andrk.dto.mapper;

import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;

import java.util.Map;

public interface ApiErrorMapper {
    ApiErrorDto fromErrorAttributes(Map<String, Object> errAttrs);

    ApiErrorDto fromOtherError(OtherLibraryManipulationException e);

    ApiErrorDto fromKnownError(KnownLibraryManipulationException e);

}
