package ru.demo.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.demo.dto.NotificationDto;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @CircuitBreaker(name = "serviceCircuitBreaker", fallbackMethod = "fallback")
    public void send(NotificationDto notificationDto) {
        System.out.printf(
                "Sending notification to user %s: %s%n",
                notificationDto.getUserId(),
                notificationDto.getMessage()
        );
    }

    public void fallback(NotificationDto notificationDto, Throwable ex) {
        System.out.printf(
                "Notification service unavailable for user: %s",
                notificationDto.getUserId()
        );
    }
}
