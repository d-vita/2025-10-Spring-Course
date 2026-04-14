package ru.otus.hw.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.kafka.event.UserTariffUpdatedEvent;
import ru.otus.hw.models.UserTariffCache;
import ru.otus.hw.repositories.UserTariffCacheRepository;

@RequiredArgsConstructor
@Component
public class UserTariffUpdatedEventConsumer {

    private final UserTariffCacheRepository userTariffCacheRepository;

    @KafkaListener(topics = "user-tariff-updated", groupId = "stat-service-group")
    @Transactional
    public void consumeUserTariffUpdatedEvent(UserTariffUpdatedEvent event) {
        UserTariffCache cache = userTariffCacheRepository.findById(event.getUserId())
                .orElse(new UserTariffCache());

        cache.setUserId(event.getUserId());
        cache.setTariffName(event.getTariffName());
        cache.setMaxClicksPerLink(event.getMaxClicksPerLink());
        cache.setMaxLinks(event.getMaxLinks());

        userTariffCacheRepository.save(cache);
    }
}
