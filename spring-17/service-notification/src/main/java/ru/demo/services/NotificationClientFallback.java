package ru.demo.services;

import org.springframework.stereotype.Component;
import ru.demo.dto.NotificationRequest;

@Component
public class NotificationClientFallback implements NotificationClient {

    @Override
    public void sendNotification(NotificationRequest request) {
        System.out.println("Notification service unavailable. Skipping notification.");
    }
}
