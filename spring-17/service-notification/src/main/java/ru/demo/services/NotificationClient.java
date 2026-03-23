package ru.demo.services;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.demo.dto.NotificationRequest;

@FeignClient(
        name = "notification-service",
        url = "http://localhost:8081",
        fallback = NotificationClientFallback.class
)
public interface NotificationClient {
    @PostMapping("/notifications")
    void sendNotification(@RequestBody NotificationRequest request);
}
