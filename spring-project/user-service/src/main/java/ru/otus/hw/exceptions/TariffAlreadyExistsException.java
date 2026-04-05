package ru.otus.hw.exceptions;

public class TariffAlreadyExistsException extends RuntimeException {
    public TariffAlreadyExistsException(String message) {
        super(message);
    }
}
