package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.NotificationDto;
import ru.otus.hw.models.Notification;

@Component
public class NotificationConverter {

    public NotificationDto fromDomainObject(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getUserId(),
                notification.getShortUrl(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
