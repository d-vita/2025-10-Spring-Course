package ru.otus.hw.services;

import ru.otus.hw.domain.BookRequest;

public interface RequestService {

    BookRequest validate(BookRequest request);
}
