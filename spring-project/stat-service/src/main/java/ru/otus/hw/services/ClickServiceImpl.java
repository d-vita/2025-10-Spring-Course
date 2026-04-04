package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.UserConverter;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Click;
import ru.otus.hw.repositories.ClickRepository;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClickServiceImpl implements ClickService {

    private final ClickRepository clickStatRepository;
    private final UserClient userClient; // Feign REST client для получения тарифа
    private final NotificationService notificationService;

    public void recordClick(String shortUrl, Long userId) {
        Click stat = clickStatRepository.findById(shortUrl + ":" + userId)
                .orElse(new Click(shortUrl + ":" + userId, userId, shortUrl, 0, Instant.now()));

        stat.setClicks(stat.getClicks() + 1);
        stat.setLastClickAt(Instant.now());
        clickStatRepository.save(stat);

        long maxClicks = userClient.getUserTariffLimit(userId);
        if (stat.getClicks() > maxClicks) {
            notificationService.sendLimitExceeded(userId);
        }
    }

}
