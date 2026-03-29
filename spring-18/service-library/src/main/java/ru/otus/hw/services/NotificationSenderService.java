package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.NotificationDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationSenderService {

    private final NotificationService notificationService;

    @CircuitBreaker(name = "serviceCircuitBreaker", fallbackMethod = "fallback")
    public void send(NotificationDto notificationDto) {
        notificationService.send(notificationDto);
    }

    public void fallback(NotificationDto notificationDto, Throwable ex) {
        log.warn("Notification service unavailable for user {}. Reason: {}",
                notificationDto.userId(), ex.getMessage());
    }
}
