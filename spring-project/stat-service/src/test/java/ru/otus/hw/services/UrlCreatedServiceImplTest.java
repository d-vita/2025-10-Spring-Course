package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.models.UrlCreated;
import ru.otus.hw.models.UserTariffCache;
import ru.otus.hw.repositories.UrlCreatedRepository;
import ru.otus.hw.repositories.UserTariffCacheRepository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("URL Created Service Implementation Tests")
class UrlCreatedServiceImplTest {

    @Mock
    private UrlCreatedRepository urlCreatedRepository;

    @Mock
    private UserTariffCacheRepository userTariffCacheRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private UrlCreatedServiceImpl urlCreatedService;

    private Long userId;
    private UrlCreated testUrlCreated;

    @BeforeEach
    void setUp() {
        userId = 1L;
        testUrlCreated = new UrlCreated(userId, 5L, Instant.now());
    }

    @Test
    @DisplayName("Should record first URL creation for user")
    void shouldRecordFirstUrlCreation() {
        when(urlCreatedRepository.findById(userId)).thenReturn(Optional.empty());
        when(urlCreatedRepository.save(any(UrlCreated.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userTariffCacheRepository.findById(userId)).thenReturn(Optional.empty());

        urlCreatedService.recordUrlCreation(userId);

        verify(urlCreatedRepository).save(any(UrlCreated.class));
    }

    @Test
    @DisplayName("Should increment existing URL creation count")
    void shouldIncrementExistingUrlCreationCount() {
        UrlCreated existing = new UrlCreated(userId, 3L, Instant.now());

        when(urlCreatedRepository.findById(userId)).thenReturn(Optional.of(existing));
        when(urlCreatedRepository.save(any(UrlCreated.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userTariffCacheRepository.findById(userId)).thenReturn(Optional.empty());

        urlCreatedService.recordUrlCreation(userId);

        assertThat(existing.getUrlsCreated()).isEqualTo(4L);
        verify(urlCreatedRepository).save(existing);
    }

    @Test
    @DisplayName("Should create notification when URL creation limit exceeded")
    void shouldCreateNotificationWhenLimitExceeded() {
        UrlCreated urlCreated = new UrlCreated(userId, 9L, Instant.now());
        UserTariffCache tariff = new UserTariffCache();
        tariff.setUserId(userId);
        tariff.setMaxLinks(5L);

        when(urlCreatedRepository.findById(userId)).thenReturn(Optional.of(urlCreated));
        when(urlCreatedRepository.save(any(UrlCreated.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userTariffCacheRepository.findById(userId)).thenReturn(Optional.of(tariff));

        urlCreatedService.recordUrlCreation(userId);

        verify(notificationService).createNotification(anyLong(), anyString(), anyString());
        assertThat(urlCreated.getUrlsCreated()).isEqualTo(10L);
    }

    @Test
    @DisplayName("Should not create notification when under limit")
    void shouldNotCreateNotificationWhenUnderLimit() {
        UrlCreated urlCreated = new UrlCreated(userId, 2L, Instant.now());
        UserTariffCache tariff = new UserTariffCache();
        tariff.setUserId(userId);
        tariff.setMaxLinks(10L);

        when(urlCreatedRepository.findById(userId)).thenReturn(Optional.of(urlCreated));
        when(urlCreatedRepository.save(any(UrlCreated.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userTariffCacheRepository.findById(userId)).thenReturn(Optional.of(tariff));

        urlCreatedService.recordUrlCreation(userId);

        verify(notificationService, never()).createNotification(anyLong(), anyString(), anyString());
        assertThat(urlCreated.getUrlsCreated()).isEqualTo(3L);
    }

    @Test
    @DisplayName("Should get URLs created count")
    void shouldGetUrlsCreatedCount() {
        when(urlCreatedRepository.findById(userId)).thenReturn(Optional.of(testUrlCreated));

        long count = urlCreatedService.getUrlsCreated(userId);

        assertThat(count).isEqualTo(5L);
        verify(urlCreatedRepository).findById(userId);
    }

    @Test
    @DisplayName("Should return zero when no URLs created")
    void shouldReturnZeroWhenNoUrlsCreated() {
        when(urlCreatedRepository.findById(userId)).thenReturn(Optional.empty());

        long count = urlCreatedService.getUrlsCreated(userId);

        assertThat(count).isEqualTo(0L);
    }

    @Test
    @DisplayName("Should update lastCreatedAt timestamp")
    void shouldUpdateLastCreatedAtTimestamp() {
        Instant oldTimestamp = Instant.now().minusSeconds(3600);
        UrlCreated urlCreated = new UrlCreated(userId, 1L, oldTimestamp);

        when(urlCreatedRepository.findById(userId)).thenReturn(Optional.of(urlCreated));
        when(urlCreatedRepository.save(any(UrlCreated.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userTariffCacheRepository.findById(userId)).thenReturn(Optional.empty());

        urlCreatedService.recordUrlCreation(userId);

        assertThat(urlCreated.getLastCreatedAt()).isAfter(oldTimestamp);
    }

    @Test
    @DisplayName("Should not create notification when tariff not found")
    void shouldNotCreateNotificationWhenTariffNotFound() {
        when(urlCreatedRepository.findById(userId)).thenReturn(Optional.of(testUrlCreated));
        when(urlCreatedRepository.save(any(UrlCreated.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userTariffCacheRepository.findById(userId)).thenReturn(Optional.empty());

        urlCreatedService.recordUrlCreation(userId);

        verify(notificationService, never()).createNotification(anyLong(), anyString(), anyString());
    }
}
