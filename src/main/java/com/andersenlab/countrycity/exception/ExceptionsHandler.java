package com.andersenlab.countrycity.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@Slf4j
public class ExceptionsHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadException(MaxUploadSizeExceededException ex) {
        log.warn("Received too big uploaded file. {}", ex.getCause().getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
