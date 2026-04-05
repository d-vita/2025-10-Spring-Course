package ru.otus.hw.services;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Click;
import ru.otus.hw.repositories.ClickRepository;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class ClickServiceImpl implements ClickService {

    private final ClickRepository clickRepository;

//    private final UserClient userClient;
//
//    private final NotificationService notificationService;

    @Override
    @Transactional
    public void recordClick(String shortUrl, Long userId) {
        String id = shortUrl + ":" + userId;
        Click click = clickRepository.findById(id)
                .orElse(new Click(id, userId, shortUrl, 0, Instant.now()));

        click.setClicks(click.getClicks() + 1);
        click.setLastClickAt(Instant.now());

        clickRepository.save(click);

//        long maxClicks = userClient.getUserTariffLimit(userId);
//        if (click.getClicks() > maxClicks) {
//            notificationService.sendLimitExceeded(userId);
//        }
    }

    @Override
    public long getClicks(Long userId, String shortUrl) {
        return clickRepository.findByUserIdAndShortUrl(userId, shortUrl)
                .map(Click::getClicks)
                .orElse(0L);
    }

}
