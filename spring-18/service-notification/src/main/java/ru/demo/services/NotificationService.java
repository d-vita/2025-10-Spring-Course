package ru.demo.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.demo.dto.NotificationDto;

@Service
@Slf4j
public class NotificationService {

    @CircuitBreaker(name = "serviceCircuitBreaker", fallbackMethod = "fallback")
    public void send(NotificationDto notificationDto) {
        log.info("Sending notification to user {}: {}" ,
                notificationDto.getUserId(),
                notificationDto.getMessage());

//         симуляция ошибки для теста CircuitBreaker
//        throw new RuntimeException("Simulated notification failure");
    }

    public void fallback(NotificationDto notificationDto, Throwable ex) {
        log.warn("Notification service unavailable for user {}. Reason: {}",
                notificationDto.getUserId(),
                ex.getMessage());
    }
}
