package ru.otus.hw.services;

import ru.otus.hw.domain.BookOrderRequest;

public interface OrderService {

    void validate(BookOrderRequest request);
}
