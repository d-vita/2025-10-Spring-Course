package com.urlshortener.constants;


import java.time.Duration;

/**
 * Application constants: shortener domain, cache TTL, hashing parameters.
 */

public class Constants {
    private Constants(){}
    public static final String DOMAIN = "https://short.ly/";
    public static final Duration TTL = Duration.ofDays(30);

    public static final int MAX_ATTEMPTS = 10;
    public static final int SHORT_BYTES_LENGTH = 6;
    public static final String HASH_ALGORITHM = "SHA-256";

    public static final String SHORT_KEY_PREFIX = "short:";
    public static final String LONG_KEY_PREFIX = "long:";
}
