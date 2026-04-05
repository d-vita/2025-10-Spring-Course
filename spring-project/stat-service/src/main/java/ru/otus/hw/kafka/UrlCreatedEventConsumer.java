package ru.otus.hw.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.hw.event.ClickEvent;
import ru.otus.hw.event.UrlCreatedEvent;
import ru.otus.hw.services.ClickService;
import ru.otus.hw.services.UrlCreatedService;

@RequiredArgsConstructor
@Component
public class UrlCreatedEventConsumer {

    private final UrlCreatedService urlCreatedService;

    @KafkaListener(topics = "url-created", groupId = "url-created-stats-group")
    public void consumeUrlCreatedEvent(UrlCreatedEvent event) {
        urlCreatedService.recordUrlCreation(event.getUserId());
    }
}
