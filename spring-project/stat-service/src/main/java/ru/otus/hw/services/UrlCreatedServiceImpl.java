package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Click;
import ru.otus.hw.models.UrlCreated;
import ru.otus.hw.repositories.UrlCreatedRepository;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class UrlCreatedServiceImpl implements UrlCreatedService {

    private final UrlCreatedRepository urlCreatedRepository;

//    private final UserClient userClient;
//
//    private final NotificationService notificationService;

    @Override
    public void recordUrlCreation(Long userId) {
        UrlCreated urlCreated = urlCreatedRepository.findById(userId)
                .orElse(new UrlCreated(userId, 0L, Instant.now()));

        urlCreated.setUrlsCreated(urlCreated.getUrlsCreated() + 1);
        urlCreated.setLastCreatedAt(Instant.now());

        urlCreatedRepository.save(urlCreated);
    }

    @Override
    public long getUrlsCreated(Long userId) {
        return urlCreatedRepository.findById(userId)
                .map(UrlCreated::getUrlsCreated)
                .orElse(0L);
    }
}
