package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Shipment {
    Long shipmentId;
    Order order;
    String carrier;
    String trackingNumber;
}
