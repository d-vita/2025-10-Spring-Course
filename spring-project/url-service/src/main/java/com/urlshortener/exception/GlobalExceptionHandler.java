package com.urlshortener.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UniqueHashGenerationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUniqueHashGenerationException(UniqueHashGenerationException ex){
        log.error("Hash generation error: {}", ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchElementException(NoSuchElementException ex){
        log.error("Short URL not found: {}", ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Invalid URL: {}", ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAll(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return ex.getMessage();
    }
}
