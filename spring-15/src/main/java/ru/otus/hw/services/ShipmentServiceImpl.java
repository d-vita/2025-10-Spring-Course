package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Order;
import ru.otus.hw.domain.OrderStatus;
import ru.otus.hw.domain.Shipment;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class ShipmentServiceImpl implements ShipmentService {
    AtomicLong shipmentCounter = new AtomicLong(1);

    @Override
    public Shipment delivery(Order order) {
        String carrier = order.getRequest().isVip() ? "EXPRESS_COURIER" : "STANDARD_COURIER";

        String tracking = generateTrackingNumber(order.getOrderId(), shipmentCounter.get());

        order.setStatus(OrderStatus.SHIPPED);

        log.info("Shipping order={} carrier={} tracking={}",
                order.getOrderId(),
                carrier,
                tracking);

        return new Shipment(
                shipmentCounter.getAndIncrement(),
                order,
                carrier,
                tracking
        );
    }

    private String generateTrackingNumber(Long orderId, long counter) {
        return "BK-" + orderId + "-" + counter + "-" + System.currentTimeMillis();
    }
}
