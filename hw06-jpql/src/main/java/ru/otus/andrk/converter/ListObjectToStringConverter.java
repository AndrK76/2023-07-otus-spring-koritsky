package ru.otus.andrk.converter;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListObjectToStringConverter implements Converter<List<Object>, String> {

    private final ConversionService conversionService;

    public ListObjectToStringConverter(@Lazy ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public String convert(List<Object> elements) {
        StringBuilder sb = new StringBuilder();
        elements.forEach(r -> sb.append(convertToString(r)).append("\n"));
        return sb.toString();
    }

    String convertToString(Object obj) {
        return conversionService.convert(obj, String.class);
    }
}
