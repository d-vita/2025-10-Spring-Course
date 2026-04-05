package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.NotificationDto;
import ru.otus.hw.models.Notification;
import ru.otus.hw.services.NotificationService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public List<NotificationDto> getAllNotifications(@PathVariable Long userId) {
        return notificationService.getAllNotifications(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/user/{userId}/unread")
    public List<NotificationDto> getUnreadNotifications(@PathVariable Long userId) {
        return notificationService.getUnreadNotifications(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @PostMapping("/{notificationId}/mark-read")
    public void markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
    }

    private NotificationDto toDto(Notification notification) {
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
