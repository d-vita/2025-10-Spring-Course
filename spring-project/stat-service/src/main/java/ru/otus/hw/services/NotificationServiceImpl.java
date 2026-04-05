package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Notification;
import ru.otus.hw.repositories.NotificationRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void sendLimitExceeded(Long userId, String shortUrl, long currentClicks, long limit) {
        String message = String.format(
                "Превышен лимит кликов для URL %s. Текущее количество: %d, лимит: %d",
                shortUrl,
                currentClicks,
                limit
        );

        Notification notification = new Notification(userId, shortUrl, message);
        notificationRepository.save(notification);

        log.info("Created limit exceeded notification for userId: {}, shortUrl: {}", userId, shortUrl);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getAllNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
}
