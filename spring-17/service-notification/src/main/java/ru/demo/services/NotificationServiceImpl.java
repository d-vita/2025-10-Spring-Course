package ru.demo.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.demo.models.Notification;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl {

    private final NotificationService notificationClient;


    @CircuitBreaker(name = "notificationService", fallbackMethod = "fallbackSend")
    public void send(Long userId, String message) {
        Notification request = new Notification();
        request.setUserId(userId);
        request.setMessage(message);

        notificationClient.sendNotification(request);
    }

    public void fallbackSend(Long userId, String message, Throwable ex) {
        System.out.println("Fallback: notification not sent");
    }
}
