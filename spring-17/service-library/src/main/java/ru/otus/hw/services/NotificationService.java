package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.client.NotificationClient;
import ru.otus.hw.dto.NotificationDto;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationClient notificationClient;

    public void send(NotificationDto notificationDto) {
        notificationClient.sendNotification(notificationDto);
    }
}
