package com.urlshortener.service.kafka;

import com.urlshortener.event.UrlCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UrlCreatedEventProducer {

    private static final String TOPIC = "url-created";

    private final KafkaTemplate<String, UrlCreatedEvent> kafkaTemplate;

    public void sendUrlCreatedEvent(String shortUrl, String longUrl, Long userId) {
        UrlCreatedEvent event = new UrlCreatedEvent(
                shortUrl,
                longUrl,
                userId,
                Instant.now()
        );

        kafkaTemplate.send(TOPIC, shortUrl, event);
    }
}
