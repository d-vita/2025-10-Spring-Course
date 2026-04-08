package ru.otus.hw.services;

import ru.otus.hw.models.Notification;

import java.util.List;

public interface NotificationService {

    void createNotification(Long userId, String shortUrl, String message);

    List<Notification> getUnreadNotifications(Long userId);

    List<Notification> getAllNotifications(Long userId);

    void markAsRead(Long notificationId);
}
