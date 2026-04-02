package com.urlshortener.service.hashgenerator;

import com.urlshortener.exception.UniqueHashGenerationException;
import com.urlshortener.repository.StorageRepository;
import com.urlshortener.repository.cache.CacheRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import static com.urlshortener.constants.Constants.*;

/**
 * Generates short hash codes for URLs using SHA-256 + Base62 and ensures uniqueness.
 * Retries with random byte changes up to MAX_ATTEMPTS if collisions occur.
 */
@Service
@AllArgsConstructor
public class HashGeneratorImpl implements HashGenerator {
    private final CacheRepository<String, String> cache;
    private final StorageRepository storage;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String encode(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));

            byte[] shortBytes = Arrays.copyOf(digest, SHORT_BYTES_LENGTH);

            String hash = Base62.encode(shortBytes);
            int attempt = 0;

            while (isHashExist(hash) && attempt < MAX_ATTEMPTS) {
                int randomIndex = RANDOM.nextInt(shortBytes.length);
                shortBytes[randomIndex] = (byte) RANDOM.nextInt(256);

                hash = Base62.encode(shortBytes);
                attempt++;
            }

            if (attempt >= MAX_ATTEMPTS) {
                throw new UniqueHashGenerationException("Failed to generate unique short URL after " + MAX_ATTEMPTS + " attempts");
            }

            return hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algorithm not found: " + HASH_ALGORITHM, e);
        }
    }

    /**
     * Check if hash is unique:
     * true = unique, false = already exists
     */
    private boolean isHashExist(String hash) {
        return cache.contains(hash) || storage.findUrlDocumentByShortUrl(hash) != null;
    }
}
