package ru.otus.hw.services;

import ru.otus.hw.domain.Order;

public interface OrderPackingService {

    Order packVipOrder(Order order);

    Order packRegularOrder(Order order);
}
