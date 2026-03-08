package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.BookOrderRequest;

import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService{
    @Override
    public void validate(BookOrderRequest request) {

        Objects.requireNonNull(request.getBookId(), "BookId shouldn't be null");
        Objects.requireNonNull(request.getCustomerId(), "CustomerId shouldn't be null");
        Objects.requireNonNull(request.getShippingAddress(), "Shipping Address shouldn't be null");
    }
}
