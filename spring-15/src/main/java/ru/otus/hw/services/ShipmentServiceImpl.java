package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Order;
import ru.otus.hw.domain.OrderStatus;
import ru.otus.hw.domain.Shipment;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    AtomicLong shipmentCounter = new AtomicLong(1);

    @Override
    public Shipment delivery(Order order) {
        String carrier = order.getRequest().isVip() ? "EXPRESS_COURIER" : "STANDARD_COURIER";
        String trackingNumber = generateTrackingNumber(order.getOrderId(), shipmentCounter.get());

        order.setStatus(OrderStatus.SHIPPED);

        System.out.println(order + " " + carrier + " " + trackingNumber);
        return new Shipment(
                shipmentCounter.getAndIncrement(),
                order,
                carrier,
                trackingNumber
        );
    }

    private String generateTrackingNumber(Long orderId, long counter) {
        return "BK-" + orderId + "-" + counter + "-" + System.currentTimeMillis();
    }
}
