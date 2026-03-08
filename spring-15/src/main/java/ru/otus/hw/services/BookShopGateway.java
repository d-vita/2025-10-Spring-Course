package ru.otus.hw.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.BookRequest;
import ru.otus.hw.domain.Shipment;

import java.util.Collection;

@MessagingGateway
public interface BookShopGateway {

    @Gateway(requestChannel = "directChannel", replyChannel = "pubSubchannel")
    Collection<Shipment> process(Collection<BookRequest> orderItem);
}
