package ru.otus.hw.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.models.Click;
import ru.otus.hw.models.UserTariffCache;
import ru.otus.hw.repositories.ClickRepository;
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
@DisplayName("Click Service Implementation Tests")
class ClickServiceImplTest {

    @Mock
    private ClickRepository clickRepository;

    @Mock
    private UrlCreatedRepository urlCreatedRepository;

    @Mock
    private UserTariffCacheRepository userTariffCacheRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ClickServiceImpl clickService;

    private Long userId;
    private String shortUrl;
    private Click testClick;

    @BeforeEach
    void setUp() {
        userId = 1L;
        shortUrl = "abc123";
        testClick = new Click("abc123:1", userId, shortUrl, 5, Instant.now());
    }

    @Test
    @DisplayName("Should record first click for URL")
    void shouldRecordFirstClickForUrl() {
        String id = shortUrl + ":" + userId;
        when(clickRepository.findById(id)).thenReturn(Optional.empty());
        when(clickRepository.save(any(Click.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userTariffCacheRepository.findById(userId)).thenReturn(Optional.empty());

        clickService.recordClick(shortUrl, userId);

        verify(clickRepository).save(any(Click.class));
    }

    @Test
    @DisplayName("Should increment existing click count")
    void shouldIncrementExistingClickCount() {
        String id = shortUrl + ":" + userId;
        Click existingClick = new Click(id, userId, shortUrl, 5, Instant.now());

        when(clickRepository.findById(id)).thenReturn(Optional.of(existingClick));
        when(clickRepository.save(any(Click.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userTariffCacheRepository.findById(userId)).thenReturn(Optional.empty());

        clickService.recordClick(shortUrl, userId);

        verify(clickRepository).save(any(Click.class));
        assertThat(existingClick.getClicks()).isEqualTo(6);
    }

    @Test
    @DisplayName("Should create notification when click limit exceeded")
    void shouldCreateNotificationWhenClickLimitExceeded() {
        String id = shortUrl + ":" + userId;
        Click click = new Click(id, userId, shortUrl, 10, Instant.now());
        UserTariffCache tariff = new UserTariffCache();
        tariff.setUserId(userId);
        tariff.setMaxClicksPerLink(5L);

        when(clickRepository.findById(id)).thenReturn(Optional.of(click));
        when(clickRepository.save(any(Click.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userTariffCacheRepository.findById(userId)).thenReturn(Optional.of(tariff));

        clickService.recordClick(shortUrl, userId);

        verify(notificationService).createNotification(anyLong(), anyString(), anyString());
    }

    @Test
    @DisplayName("Should not create notification when under limit")
    void shouldNotCreateNotificationWhenUnderLimit() {
        String id = shortUrl + ":" + userId;
        Click click = new Click(id, userId, shortUrl, 3, Instant.now());
        UserTariffCache tariff = new UserTariffCache();
        tariff.setUserId(userId);
        tariff.setMaxClicksPerLink(10L);

        when(clickRepository.findById(id)).thenReturn(Optional.of(click));
        when(clickRepository.save(any(Click.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userTariffCacheRepository.findById(userId)).thenReturn(Optional.of(tariff));

        clickService.recordClick(shortUrl, userId);

        verify(notificationService, never()).createNotification(anyLong(), anyString(), anyString());
    }

    @Test
    @DisplayName("Should get click count")
    void shouldGetClickCount() {
        when(clickRepository.findByUserIdAndShortUrl(userId, shortUrl))
                .thenReturn(Optional.of(testClick));

        long clicks = clickService.getClicks(userId, shortUrl);

        assertThat(clicks).isEqualTo(5);
    }

    @Test
    @DisplayName("Should return zero when no clicks recorded")
    void shouldReturnZeroWhenNoClicksRecorded() {
        when(clickRepository.findByUserIdAndShortUrl(userId, shortUrl))
                .thenReturn(Optional.empty());

        long clicks = clickService.getClicks(userId, shortUrl);

        assertThat(clicks).isEqualTo(0);
    }

    @Test
    @DisplayName("Should anonymize user clicks")
    void shouldAnonymizeUserClicks() {
        clickService.anonymizeUserClicks(userId);

        verify(clickRepository).anonymizeByUserId(userId);
        verify(urlCreatedRepository).anonymizeByUserId(userId);
    }
}