package ru.otus.andrk.config.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import ru.otus.andrk.dto.mapper.ApiErrorMapper;

import java.util.Map;

@Log4j2
@Component
@RequiredArgsConstructor
public class CustomErrorAttributes extends DefaultErrorAttributes {

    private final ApiErrorMapper mapper;

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        var ret = super.getErrorAttributes(request, options);
        var errDto = mapper.fromErrorAttributes(ret);
        ret.put("statusMessage",errDto.getStatusMessage());
        ret.put("errorMessage",errDto.getErrorMessage());
        ret.put("detailMessage",errDto.getDetailMessage());
        return ret;
    }
}
