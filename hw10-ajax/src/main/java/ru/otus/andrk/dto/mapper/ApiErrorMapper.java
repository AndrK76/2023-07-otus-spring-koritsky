package ru.otus.andrk.dto.mapper;

import ru.otus.andrk.dto.ApiErrorDto;

import java.util.Map;

public interface ApiErrorMapper {
 ApiErrorDto fromErrorAttributes(Map<String, Object> errAttrs);


}
