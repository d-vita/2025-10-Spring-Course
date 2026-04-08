package ru.otus.hw.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.kafka.event.UserDeletedEvent;

import java.time.Instant;

import static org.reflections.Reflections.log;

@Service
@RequiredArgsConstructor
public class UserDeletedEventProducer {

    private static final String TOPIC = "user-deleted";

    private final KafkaTemplate<String, UserDeletedEvent> kafkaTemplate;

    public void sendUserDeletedEvent(Long userId, String username) {
        log.info("!!!!!!!!!!UserDeletedEvent {} {}", username, userId);
        UserDeletedEvent event = new UserDeletedEvent(
                userId,
                username,
                Instant.now()
        );

        kafkaTemplate.send(TOPIC, userId.toString(), event);
    }
}