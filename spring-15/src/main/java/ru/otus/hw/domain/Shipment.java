package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Shipment {

    private Long shipmentId;

    private Order order;

    private String carrier;

    private String trackingNumber;
}
