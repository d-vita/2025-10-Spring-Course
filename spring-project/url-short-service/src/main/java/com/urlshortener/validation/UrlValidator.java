package com.urlshortener.validation;


import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;


public class UrlValidator {

    private UrlValidator() {
    }

    public static boolean isNotValidUrl(String url) {
        if (url == null || url.isBlank()) {
            return true;
        }

        URI uri = parseUri(url);
        if (uri == null) {
            return true;
        }

        if (!isValidScheme(uri)) {
            return true;
        }

        String host = uri.getHost();
        if (host == null || host.isBlank()) {
            return true;
        }

        return isLocalOrInvalidHost(host);
    }

    private static URI parseUri(String url) {
        try {
            return new URI(url.trim());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private static boolean isValidScheme(URI uri) {
        String scheme = uri.getScheme();
        return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
    }

    private static boolean isLocalOrInvalidHost(String host) {
        try {
            InetAddress address = InetAddress.getByName(host);
            return address.isLoopbackAddress() || address.isAnyLocalAddress() || address.isSiteLocalAddress();
        } catch (UnknownHostException e) {
            return true;
        }
    }
}