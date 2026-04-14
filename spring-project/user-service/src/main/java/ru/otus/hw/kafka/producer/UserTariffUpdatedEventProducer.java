package ru.otus.hw.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.kafka.event.UserTariffUpdatedEvent;
import ru.otus.hw.models.User;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserTariffUpdatedEventProducer {

    private static final String TOPIC = "user-tariff-updated";

    private final KafkaTemplate<String, UserTariffUpdatedEvent> kafkaTemplate;

    public void sendUserTariffUpdatedEvent(User user) {
        UserTariffUpdatedEvent event = new UserTariffUpdatedEvent(
                user.getId(),
                user.getTariff().getName(),
                user.getTariff().getMaxClicksPerLink(),
                user.getTariff().getMaxLinks(),
                Instant.now()
        );

        kafkaTemplate.send(TOPIC, user.getId().toString(), event);
    }
}
