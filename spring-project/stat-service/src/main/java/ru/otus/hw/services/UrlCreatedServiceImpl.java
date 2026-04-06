package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.UrlCreated;
import ru.otus.hw.repositories.UrlCreatedRepository;
import ru.otus.hw.repositories.UserTariffCacheRepository;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class UrlCreatedServiceImpl implements UrlCreatedService {

    private final UrlCreatedRepository urlCreatedRepository;

    private final UserTariffCacheRepository userTariffCacheRepository;

    private final NotificationService notificationService;

    @Override
    public void recordUrlCreation(Long userId) {
        UrlCreated urlCreated = urlCreatedRepository.findById(userId)
                .orElse(new UrlCreated(userId, 0L, Instant.now()));

        urlCreated.setUrlsCreated(urlCreated.getUrlsCreated() + 1);
        urlCreated.setLastCreatedAt(Instant.now());

        urlCreatedRepository.save(urlCreated);
        checkUrlCreationLimit(userId, urlCreated.getUrlsCreated());
    }

    @Override
    public long getUrlsCreated(Long userId) {
        return urlCreatedRepository.findById(userId)
                .map(UrlCreated::getUrlsCreated)
                .orElse(0L);
    }

    private void checkUrlCreationLimit(Long userId, long currentUrlsCreated) {
        userTariffCacheRepository.findById(userId).ifPresent(tariff -> {
            if (currentUrlsCreated > tariff.getMaxLinks()) {
                String message = String.format(
                        "The limit of created URLs has been exceeded. Current number: %d, limit: %d",
                        currentUrlsCreated,
                        tariff.getMaxLinks()
                );

                notificationService.createNotification(userId, "", message);
            }
        });
    }
}
