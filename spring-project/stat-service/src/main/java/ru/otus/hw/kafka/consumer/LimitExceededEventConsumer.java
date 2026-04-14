package ru.otus.hw.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.hw.kafka.event.LimitExceededEvent;
import ru.otus.hw.services.NotificationService;

@RequiredArgsConstructor
@Component
public class LimitExceededEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "limit-exceeded", groupId = "stat-service-group")
    public void consumeLimitExceededEvent(LimitExceededEvent event) {
        String message = createNotificationMessage(event);
        notificationService.createNotification(
                event.getUserId(),
                event.getShortUrl(),
                message
        );
    }

    private String createNotificationMessage(LimitExceededEvent event) {
        return switch (event.getLimitType()) {
            case CLICKS -> String.format(
                    "The limit has been exceeded for URL %s. Current number: %d, limit: %d",
                    event.getShortUrl(),
                    event.getCurrentValue(),
                    event.getLimitValue()
            );
            case URLS_CREATED -> String.format(
                    "The limit of created URLs has been exceeded. Current number: %d, limit: %d",
                    event.getCurrentValue(),
                    event.getLimitValue()
            );
        };
    }
}
