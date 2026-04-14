package ru.otus.hw.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.kafka.event.UserRegisteredEvent;
import ru.otus.hw.models.User;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserRegisteredEventProducer {

    private static final String TOPIC = "user-registered";

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    public void sendUserRegisteredEvent(User user) {
        UserRegisteredEvent event = new UserRegisteredEvent(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getTariff().getName(),
                user.getTariff().getMaxClicksPerLink(),
                user.getTariff().getMaxLinks(),
                Instant.now()
        );

        kafkaTemplate.send(TOPIC, user.getId().toString(), event);
    }
}
