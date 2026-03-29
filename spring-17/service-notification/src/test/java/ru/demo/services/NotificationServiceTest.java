package ru.demo.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.demo.dto.NotificationDto;


@SpringBootTest
class NotificationServiceTest {

    @Test
    void shouldCallFallback_whenExceptionThrown() {
        NotificationDto dto = new NotificationDto(1L, "Test message");

        NotificationService failingService = new NotificationService() {
            @Override
            public void send(NotificationDto notificationDto) {
                throw new RuntimeException("Simulated failure");
            }
        };

        failingService.fallback(dto, new RuntimeException("Simulated failure"));
    }
}