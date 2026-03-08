package ru.otus.hw.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Order {
    Long orderId;
    BookRequest request;
    OrderStatus status;
}
