package ru.otus.hw.services;


public interface ClickService {

    void recordClick(String shortUrl, Long userId);

    long getClicks(Long userId, String shortUrl);

    void anonymizeUserClicks(Long userId);
}
