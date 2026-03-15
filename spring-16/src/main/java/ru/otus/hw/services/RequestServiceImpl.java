package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.BookRequest;

import java.util.Objects;

@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    @Override
    public BookRequest validate(BookRequest request) {

        Objects.requireNonNull(request.getBookId(), "BookId shouldn't be null");
        Objects.requireNonNull(request.getCustomerId(), "CustomerId shouldn't be null");
        Objects.requireNonNull(request.getShippingAddress(), "Shipping Address shouldn't be null");

        log.info("Request is valid: bookId={}", request.getBookId());
        return request;
    }
}
