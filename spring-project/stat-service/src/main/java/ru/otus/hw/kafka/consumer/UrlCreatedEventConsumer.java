package ru.otus.hw.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.hw.kafka.event.UrlCreatedEvent;
import ru.otus.hw.services.UrlCreatedService;


@RequiredArgsConstructor
@Component
public class UrlCreatedEventConsumer {

    private final UrlCreatedService urlCreatedService;

    @KafkaListener(topics = "url-created", groupId = "stat-service-group")
    public void consumeUrlCreatedEvent(UrlCreatedEvent event) {
        urlCreatedService.recordUrlCreation(event.getUserId());
    }
}
