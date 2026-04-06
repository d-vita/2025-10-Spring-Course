package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.models.Notification;
import ru.otus.hw.repositories.NotificationRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("Notification Service Implementation Tests")
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Long userId;
    private String shortUrl;
    private String message;
    private Notification testNotification;

    @BeforeEach
    void setUp() {
        userId = 1L;
        shortUrl = "abc123";
        message = "Test notification message";
        testNotification = new Notification(userId, shortUrl, message);
        testNotification.setId(1L);
        testNotification.setRead(false);
        testNotification.setCreatedAt(Instant.now());
    }

    @Test
    @DisplayName("Should create notification")
    void shouldCreateNotification() {
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        notificationService.createNotification(userId, shortUrl, message);

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    @DisplayName("Should get unread notifications for user")
    void shouldGetUnreadNotifications() {
        Notification notification1 = new Notification(userId, "abc123", "Message 1");
        notification1.setRead(false);
        Notification notification2 = new Notification(userId, "def456", "Message 2");
        notification2.setRead(false);

        List<Notification> unreadNotifications = List.of(notification1, notification2);

        when(notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId))
                .thenReturn(unreadNotifications);

        List<Notification> result = notificationService.getUnreadNotifications(userId);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(n -> !n.isRead());
        verify(notificationRepository).findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    @Test
    @DisplayName("Should return empty list when no unread notifications")
    void shouldReturnEmptyListWhenNoUnreadNotifications() {
        when(notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId))
                .thenReturn(List.of());

        List<Notification> result = notificationService.getUnreadNotifications(userId);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should get all notifications for user")
    void shouldGetAllNotifications() {
        Notification notification1 = new Notification(userId, "abc123", "Message 1");
        notification1.setRead(false);
        Notification notification2 = new Notification(userId, "def456", "Message 2");
        notification2.setRead(true);
        Notification notification3 = new Notification(userId, "ghi789", "Message 3");
        notification3.setRead(false);

        List<Notification> allNotifications = List.of(notification1, notification2, notification3);

        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(userId))
                .thenReturn(allNotifications);

        List<Notification> result = notificationService.getAllNotifications(userId);

        assertThat(result).hasSize(3);
        verify(notificationRepository).findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Test
    @DisplayName("Should return empty list when user has no notifications")
    void shouldReturnEmptyListWhenUserHasNoNotifications() {
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(userId))
                .thenReturn(List.of());

        List<Notification> result = notificationService.getAllNotifications(userId);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should mark notification as read")
    void shouldMarkNotificationAsRead() {
        Long notificationId = 1L;
        Notification notification = new Notification(userId, shortUrl, message);
        notification.setId(notificationId);
        notification.setRead(false);

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        notificationService.markAsRead(notificationId);

        assertThat(notification.isRead()).isTrue();
        verify(notificationRepository).save(notification);
    }

    @Test
    @DisplayName("Should do nothing when notification not found")
    void shouldDoNothingWhenNotificationNotFound() {
        Long notificationId = 999L;

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        notificationService.markAsRead(notificationId);

        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    @DisplayName("Should not change already read notification")
    void shouldNotChangeAlreadyReadNotification() {
        Long notificationId = 1L;
        Notification notification = new Notification(userId, shortUrl, message);
        notification.setId(notificationId);
        notification.setRead(true);

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        notificationService.markAsRead(notificationId);

        assertThat(notification.isRead()).isTrue();
        verify(notificationRepository).save(notification);
    }

    @Test
    @DisplayName("Should create notification with empty shortUrl")
    void shouldCreateNotificationWithEmptyShortUrl() {
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        notificationService.createNotification(userId, "", "Limit exceeded");

        verify(notificationRepository).save(any(Notification.class));
    }
}