package ru.demo.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.demo.dto.NotificationRequest;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationClient notificationClient;


    @CircuitBreaker(name = "notificationService", fallbackMethod = "fallbackSend")
    public void send(Long userId, String message) {
        NotificationRequest request = new NotificationRequest();
        request.setUserId(userId);
        request.setMessage(message);

        notificationClient.sendNotification(request);
    }

    public void fallbackSend(Long userId, String message, Throwable ex) {
        System.out.println("Fallback: notification not sent");
    }
}
