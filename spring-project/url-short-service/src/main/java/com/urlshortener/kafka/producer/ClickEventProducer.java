package com.urlshortener.kafka.producer;

import com.urlshortener.kafka.event.ClickEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ClickEventProducer {

    private static final String TOPIC = "url-clicks";

    private final KafkaTemplate<String, ClickEvent> kafkaTemplate;

    public void sendClickEvent(String shortUrl, String longUrl, Long userId) {
        ClickEvent event = new ClickEvent(
                shortUrl,
                longUrl,
                userId,
                Instant.now()
        );

        kafkaTemplate.send(TOPIC, shortUrl, event);
    }
}
