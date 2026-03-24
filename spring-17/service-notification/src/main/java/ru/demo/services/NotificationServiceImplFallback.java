package ru.demo.services;

import org.springframework.stereotype.Component;
import ru.demo.models.Notification;

@Component
public class NotificationServiceImplFallback implements NotificationService {

    @Override
    public void sendNotification(Notification notification) {
        System.out.println("Notification service unavailable. Skipping notification.");
    }
}
