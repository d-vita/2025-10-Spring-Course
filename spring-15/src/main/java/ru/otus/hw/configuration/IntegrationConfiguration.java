package ru.otus.hw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.hw.domain.BookOrderRequest;
import ru.otus.hw.domain.Order;
import ru.otus.hw.domain.OrderStatus;
import ru.otus.hw.domain.Shipment;
import ru.otus.hw.services.OrderService;
import ru.otus.hw.services.ShipmentService;

import static org.yaml.snakeyaml.nodes.NodeId.mapping;


@Configuration
public class IntegrationConfiguration {

    @Bean
    public MessageChannelSpec<?, ?> queueChannel() {
        return MessageChannels.queue(100);
    }

    @Bean
    public MessageChannelSpec<?, ?> priorityChannel() {
        return MessageChannels.priority().capacity(100);
    }

    @Bean
    public MessageChannelSpec<?, ?> pubSubchannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> directChannel() {
        return MessageChannels.direct();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers
                .fixedRate(1000)
                .maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow createOrder(OrderService orderService, ShipmentService shipmentService) {
        return IntegrationFlow.from(directChannel())
                .split()
                .<BookOrderRequest, Order>transform(req -> new Order(req.getBookId(), req, OrderStatus.NEW))
                .handle(orderService, "validate")
                .<Order, Order>transform(ord -> {
                    ord.setStatus(OrderStatus.VALIDATED);
                    return ord;
                })
                .<Order, Boolean>route(ord -> ord.getRequest().isVip(), mapping -> mapping
                        .subFlowMapping(true, subflow -> subflow.channel(priorityChannel()))
                        .subFlowMapping(false, subflow -> subflow.channel(queueChannel()))
                )
                .<Order, Order>transform(ord -> {
                    ord.setStatus(OrderStatus.PACKED);
                    return ord;
                })
                .handle(shipmentService, "delivery")
                .channel(pubSubchannel())
                .get();
    }
}
