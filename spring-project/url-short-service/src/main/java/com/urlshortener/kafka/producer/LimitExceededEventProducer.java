package com.urlshortener.kafka.producer;

import com.urlshortener.kafka.event.LimitExceededEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LimitExceededEventProducer {

    private static final String TOPIC = "limit-exceeded";

    private final KafkaTemplate<String, LimitExceededEvent> kafkaTemplate;

    public void sendLimitExceededEvent(Long userId, String shortUrl, LimitExceededEvent.LimitType limitType,
                                       long currentValue, long limitValue) {
        LimitExceededEvent event = new LimitExceededEvent(
                userId,
                shortUrl,
                limitType,
                currentValue,
                limitValue,
                Instant.now()
        );

        kafkaTemplate.send(TOPIC, userId.toString(), event);
    }
}
