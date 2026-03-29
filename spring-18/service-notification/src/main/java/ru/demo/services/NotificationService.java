package ru.demo.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.demo.dto.NotificationDto;
import ru.demo.models.Notification;
import ru.demo.repositories.NotificationRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @CircuitBreaker(name = "serviceCircuitBreaker", fallbackMethod = "fallback")
    public void save(NotificationDto notificationDto) {
        log.info("Sending notification to user {}: {}" ,
                notificationDto.getUserId(),
                notificationDto.getMessage());

      Notification notification = new Notification(notificationDto.getUserId(), notificationDto.getMessage());

      notificationRepository.save(notification);
    }

    public void fallback(NotificationDto notificationDto, Throwable ex) {
        log.warn("Notification service unavailable for user {}. Reason: {}",
                notificationDto.getUserId(),
                ex.getMessage());
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}
