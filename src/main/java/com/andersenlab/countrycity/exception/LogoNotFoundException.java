package com.andersenlab.countrycity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class LogoNotFoundException extends RuntimeException {
    public LogoNotFoundException() {
        super();
    }

    public LogoNotFoundException(String id) {
        super("Not found logo with id " + id);
    }
}
