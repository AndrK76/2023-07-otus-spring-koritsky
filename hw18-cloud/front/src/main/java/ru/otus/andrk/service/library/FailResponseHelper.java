package ru.otus.andrk.service.library;

import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import ru.otus.andrk.dto.mapper.ApiErrorDtoMapper;
import ru.otus.andrk.exception.ProcessedException;


public class FailResponseHelper {
    public static  ResponseEntity<?> processFailResponse(
            RuntimeException e, String methodName, Logger log, ApiErrorDtoMapper mapper) {
        if (e instanceof ProcessedException ex) {
            return ResponseEntity.status(ex.getData().getStatus()).body(ex.getData());
        } else {
            log.debug("{}: {}", methodName, e.getMessage());
            var data = mapper.fromFeignError(e);
            return ResponseEntity.status(data.getStatus()).body(data);
        }
    }
}
