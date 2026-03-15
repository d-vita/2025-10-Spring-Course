package ru.otus.hw.services;

import ru.otus.hw.domain.Order;
import ru.otus.hw.domain.Shipment;

public interface ShipmentService {

    Shipment delivery(Order order);
}
