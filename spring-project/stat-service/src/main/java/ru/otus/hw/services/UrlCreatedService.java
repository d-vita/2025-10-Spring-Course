package ru.otus.hw.services;


public interface UrlCreatedService {

    void recordUrlCreation(Long userId);

    long getUrlsCreated(Long userId);
}
