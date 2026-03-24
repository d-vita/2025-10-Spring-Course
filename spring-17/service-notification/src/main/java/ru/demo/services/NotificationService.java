package ru.demo.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.demo.models.Notification;

@FeignClient(value = "notification-service" , fallback = NotificationServiceImplFallback.class)
public interface NotificationService {

    @PostMapping("/notifications")
    void sendNotification(@RequestBody Notification request);
}
