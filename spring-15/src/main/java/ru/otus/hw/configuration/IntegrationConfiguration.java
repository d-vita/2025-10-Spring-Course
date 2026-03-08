package ru.otus.hw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.hw.domain.BookRequest;
import ru.otus.hw.domain.Order;
import ru.otus.hw.domain.OrderStatus;
import ru.otus.hw.services.RequestService;
import ru.otus.hw.services.ShipmentService;


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
    public IntegrationFlow createOrder(RequestService requestService, ShipmentService shipmentService) {
        return IntegrationFlow.from(directChannel())
                .split()
                .<BookRequest, BookRequest>handle(requestService, "validate")
                .<BookRequest, Order>transform(req -> new Order(req.getBookId(), req, OrderStatus.NEW))
                .<Order, Boolean>route(
                        ord -> ord.getRequest().isVip(), // VIP-флаг
                        mapping -> mapping
                                .subFlowMapping(true, sf -> sf
                                        .channel(priorityChannel())
                                        .<Order, Order>transform(o -> {
                                            o.setStatus(OrderStatus.PACKED);
                                            return o;
                                        })
                                        .handle(shipmentService, "delivery")
                                )
                                .subFlowMapping(false, sf -> sf
                                        .channel(queueChannel())
                                        .<Order, Order>transform(o -> {
                                            o.setStatus(OrderStatus.PACKED);
                                            return o;
                                        })
                                        .handle(shipmentService, "delivery")
                                )
                )
                .channel(pubSubchannel())
                .get();
    }
}
