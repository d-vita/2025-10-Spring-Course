package com.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UrlValidationException extends RuntimeException {
    public UrlValidationException(String message) {
        super(message);
    }
}
