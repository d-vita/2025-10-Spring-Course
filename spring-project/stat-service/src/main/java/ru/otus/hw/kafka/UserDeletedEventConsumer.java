package ru.otus.hw.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.hw.event.UserDeletedEvent;
import ru.otus.hw.services.ClickService;

@RequiredArgsConstructor
@Component
public class UserDeletedEventConsumer {

    private final ClickService clickService;

    @KafkaListener(topics = "user-deleted", groupId = "stat-service-group")
    public void consumeUserDeletedEvent(UserDeletedEvent event) {
        clickService.anonymizeUserClicks(event.getUserId());
    }
}
