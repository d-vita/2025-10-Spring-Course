package com.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UniqueHashGenerationException extends RuntimeException {
    public UniqueHashGenerationException(String message) {
        super(message);
    }
}
