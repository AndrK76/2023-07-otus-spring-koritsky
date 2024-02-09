package ru.otus.andrk.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.exception.ProcessedException;

@Component
@Log4j2
@RequiredArgsConstructor
public class LibraryAppClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        try (var rdr = response.body().asInputStream()) {
            var data = objectMapper.readValue(rdr, ApiErrorDto.class);
            log.debug("exception parsed as ApiErrorDto");
            return new ProcessedException(data);
        } catch (Exception e) {
            log.debug("dont parse exception as ApiErrorDto, err: {}", e.getMessage());
            return errorDecoder.decode(methodKey, response);
        }
    }
}
