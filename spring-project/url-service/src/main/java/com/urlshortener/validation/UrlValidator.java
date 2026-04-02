package com.urlshortener.validation;

import java.net.*;

public class UrlValidator {
    private UrlValidator() {
    }
    public static boolean isNotValidUrl(String url) {
        if (url == null || url.isBlank()) return true;

        try {
            URI uri = new URI(url.trim());
            String scheme = uri.getScheme();
            if (!("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))) return true;

            String host = uri.getHost();
            if (host == null || host.isBlank()) return true;

            try {
                InetAddress address = InetAddress.getByName(host);
                if (address.isLoopbackAddress() || address.isAnyLocalAddress() || address.isSiteLocalAddress())
                    return true;
            } catch (UnknownHostException e) {
                return true;
            }

            return false;
        } catch (URISyntaxException e) {
            return true;
        }
    }
}
