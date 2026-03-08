package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.BookRequest;

import java.util.Objects;

@Service
public class RequestServiceImpl implements RequestService {

    @Override
    public BookRequest validate(BookRequest request) {

        Objects.requireNonNull(request.getBookId(), "BookId shouldn't be null");
        Objects.requireNonNull(request.getCustomerId(), "CustomerId shouldn't be null");
        Objects.requireNonNull(request.getShippingAddress(), "Shipping Address shouldn't be null");

        return request;
    }
}
