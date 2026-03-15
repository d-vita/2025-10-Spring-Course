package ru.otus.hw.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.annotation.IntegrationComponentScan;
import ru.otus.hw.domain.BookRequest;
import ru.otus.hw.services.BookShopGateway;
import ru.otus.hw.services.OrderPackingServiceImpl;
import ru.otus.hw.services.RequestServiceImpl;
import ru.otus.hw.services.ShipmentServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        IntegrationConfiguration.class,
        RequestServiceImpl.class,
        OrderPackingServiceImpl.class,
        ShipmentServiceImpl.class
})
@IntegrationComponentScan(basePackages = "ru.otus.hw.services")
class IntegrationFlowTest {

    @Qualifier("bookShopGateway")
    @Autowired
    private BookShopGateway gateway;

    @Test
    void shouldProcessOrdersFlow() {

        List<BookRequest> requests = List.of(
                new BookRequest(1L, 10L, "Moscow", true)
        );

        var result = gateway.process(requests);

        assertThat(result).isNotNull().hasSize(1);

        result.forEach(shipment -> {
            assertThat(shipment.getOrder()).isNotNull();
            assertThat(shipment.getTrackingNumber()).isNotBlank();
            assertThat(shipment.getOrder().getStatus().name()).isEqualTo("SHIPPED");
        });
    }
}