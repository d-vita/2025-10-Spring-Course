package ru.otus.hw.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Order {

    private Long orderId;

    private BookRequest request;

    private OrderStatus status;
}
