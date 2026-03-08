package ru.otus.hw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
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
@EnableIntegration
@IntegrationComponentScan(basePackages = "ru.otus.hw.services")
public class IntegrationConfiguration {

    @Bean
    public MessageChannelSpec<?, ?> regularOrdersQueue() {
        return MessageChannels.queue(100);
    }

    @Bean
    public MessageChannelSpec<?, ?> vipOrdersQueue() {
        return MessageChannels.priority().capacity(100);
    }

    @Bean
    public MessageChannelSpec<?, ?> shippedOrdersChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> ordersInputChannel() {
        return MessageChannels.direct();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers
                .fixedRate(1000)
                .maxMessagesPerPoll(5);
    }

    @Bean
    public IntegrationFlow createOrder(RequestService requestService) {
        return IntegrationFlow.from(ordersInputChannel())
                .split()
                .handle(requestService, "validate")
                .<BookRequest, Order>transform(req -> new Order(req.getBookId(), req, OrderStatus.NEW))
                .<Order, Boolean>route(
                        ord -> ord.getRequest().isVip(),
                        mapping -> mapping
                                .subFlowMapping(true, sf -> sf.channel(vipOrdersQueue()))
                                .subFlowMapping(false, sf -> sf.channel(regularOrdersQueue()))
                )
                .get();
    }

    @Bean
    public IntegrationFlow vipOrdersFlow(ShipmentService shipmentService) {
        return IntegrationFlow.from(vipOrdersQueue())
                .transform((Order o) -> {
                    o.setStatus(OrderStatus.PACKED);
                    return o;
                })
                .handle(shipmentService, "delivery")
                .channel(shippedOrdersChannel())
                .get();
    }

    @Bean
    public IntegrationFlow regularOrdersFlow(ShipmentService shipmentService) {
        return IntegrationFlow.from(regularOrdersQueue())
                .transform((Order o) -> {
                    o.setStatus(OrderStatus.PACKED);
                    return o;
                })
                .handle(shipmentService, "delivery")
                .channel(shippedOrdersChannel())
                .get();
    }
}
