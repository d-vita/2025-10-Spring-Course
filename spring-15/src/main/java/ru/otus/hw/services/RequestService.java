package ru.otus.hw.services;

import ru.otus.hw.domain.BookRequest;

public interface RequestService {

    void validate(BookRequest request);
}
