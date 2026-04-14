package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.converters.NotificationConverter;
import ru.otus.hw.dto.NotificationDto;
import ru.otus.hw.services.NotificationService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    private final NotificationConverter notificationConverter;

    @GetMapping("/user/{userId}")
    public List<NotificationDto> getAllNotifications(@PathVariable Long userId) {
        return notificationService.getAllNotifications(userId)
                .stream()
                .map(notificationConverter::fromDomainObject)
                .toList();
    }

    @GetMapping("/user/{userId}/unread")
    public List<NotificationDto> getUnreadNotifications(@PathVariable Long userId) {
        return notificationService.getUnreadNotifications(userId)
                .stream()
                .map(notificationConverter::fromDomainObject)
                .toList();
    }

    @PostMapping("/{notificationId}/mark-read")
    public void markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
    }
}
