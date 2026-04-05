package ru.otus.hw.services.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.event.UserDeletedEvent;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserDeletedEventProducer {

    private static final String TOPIC = "user-deleted";

    private final KafkaTemplate<String, UserDeletedEvent> kafkaTemplate;

    public void sendUserDeletedEvent(Long userId, String username) {
        UserDeletedEvent event = new UserDeletedEvent(
                userId,
                username,
                Instant.now()
        );

        kafkaTemplate.send(TOPIC, userId.toString(), event);
    }
}