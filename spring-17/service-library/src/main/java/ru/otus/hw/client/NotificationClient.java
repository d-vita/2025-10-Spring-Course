package ru.otus.hw.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.dto.NotificationDto;

@FeignClient(name = "notification-service", url = "http://localhost:8081")
public interface NotificationClient {

    @PostMapping("/api/notifications")
    void sendNotification(@RequestBody NotificationDto notificationDto);
}
