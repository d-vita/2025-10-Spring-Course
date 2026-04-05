package ru.otus.hw.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.kafka.event.UserRegisteredEvent;
import ru.otus.hw.models.UserTariffCache;
import ru.otus.hw.repositories.UserTariffCacheRepository;

@RequiredArgsConstructor
@Component
public class UserRegisteredEventConsumer {

    private final UserTariffCacheRepository userTariffCacheRepository;

    @KafkaListener(topics = "user-registered", groupId = "stat-service-group")
    @Transactional
    public void consumeUserRegisteredEvent(UserRegisteredEvent event) {
        UserTariffCache cache = new UserTariffCache(
                event.getUserId(),
                event.getMaxClicksPerLink(),
                event.getMaxLinks(),
                event.getTariffName()
        );

        userTariffCacheRepository.save(cache);
    }
}
