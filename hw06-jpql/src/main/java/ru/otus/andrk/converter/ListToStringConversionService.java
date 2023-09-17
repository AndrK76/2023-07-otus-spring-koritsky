package ru.otus.andrk.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListToStringConversionService {

    private final ConversionService conversionService;

    public String convert(List<? extends Object> elements) {
        StringBuilder sb = new StringBuilder();
        elements.forEach(r -> sb.append(convertToString(r)).append("\n"));
        return sb.toString();
    }

    String convertToString(Object obj) {
        return conversionService.convert(obj, String.class);
    }
}
