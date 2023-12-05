package ru.otus.andrk.config.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import ru.otus.andrk.dto.mapper.ApiErrorDtoConverter;


@RequiredArgsConstructor
@Log4j2
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler, AuthenticationEntryPoint {

    private final ApiErrorDtoConverter converter;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) {
        var status = HttpStatus.UNAUTHORIZED;
        var ret = converter.fromErrorResponse(request, response, exception, status);
        log.debug(ret, exception);
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {
        this.onAuthenticationFailure(request, response, authException);
    }
}
