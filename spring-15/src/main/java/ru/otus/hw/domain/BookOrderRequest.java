package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BookOrderRequest {
    Long bookId;
    Long customerId;
    String shippingAddress;
    boolean isVip;
}
