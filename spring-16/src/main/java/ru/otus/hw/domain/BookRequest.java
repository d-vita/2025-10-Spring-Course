package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BookRequest {

    private Long bookId;

    private Long customerId;

    private String shippingAddress;

    private boolean isVip;
}
