package ru.otus.hw.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Order {
    Long orderId;
    BookOrderRequest request;
    OrderStatus status;
}
