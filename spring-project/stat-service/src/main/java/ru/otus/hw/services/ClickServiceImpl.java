package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Click;
import ru.otus.hw.repositories.ClickRepository;
import ru.otus.hw.repositories.UrlCreatedRepository;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class ClickServiceImpl implements ClickService {

    private final ClickRepository clickRepository;
    private final UrlCreatedRepository urlCreatedRepository;

    @Override
    @Transactional
    public void recordClick(String shortUrl, Long userId) {
        String id = shortUrl + ":" + userId;
        Click click = clickRepository.findById(id)
                .orElse(new Click(id, userId, shortUrl, 0, Instant.now()));

        click.setClicks(click.getClicks() + 1);
        click.setLastClickAt(Instant.now());

        clickRepository.save(click);
    }

    @Override
    public long getClicks(Long userId, String shortUrl) {
        return clickRepository.findByUserIdAndShortUrl(userId, shortUrl)
                .map(Click::getClicks)
                .orElse(0L);
    }

    @Override
    @Transactional
    public void anonymizeUserClicks(Long userId) {
        clickRepository.anonymizeByUserId(userId);
        urlCreatedRepository.anonymizeByUserId(userId);
    }

}
