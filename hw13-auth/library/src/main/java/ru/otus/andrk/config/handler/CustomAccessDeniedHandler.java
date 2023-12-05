package ru.otus.andrk.config.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.mapper.ApiErrorDtoConverter;

@RequiredArgsConstructor
@Log4j2
//TODO: спросить про место
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ApiErrorDtoConverter converter;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) {
        var status = HttpStatus.FORBIDDEN;
        ApiErrorDto ret = converter.fromErrorResponse(request, response, exception, status);
        log.debug(ret);
    }

}
