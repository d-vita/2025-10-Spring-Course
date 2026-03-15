package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Order;
import ru.otus.hw.domain.OrderStatus;

@Service
@Slf4j
public class OrderPackingServiceImpl implements OrderPackingService {

    @Override
    public Order packVipOrder(Order order) {
        order.setStatus(OrderStatus.PACKED);
        log.info("VIP order packed: {}", order.getOrderId());
        return order;
    }

    @Override
    public Order packRegularOrder(Order order) {
        order.setStatus(OrderStatus.PACKED);
        log.info("Regular order packed: {}", order.getOrderId());
        return order;
    }
}
