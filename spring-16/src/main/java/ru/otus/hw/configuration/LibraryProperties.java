package ru.otus.hw.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "library")
public class LibraryProperties {

    private Thresholds thresholds;

    private Message message;

    @Data
    public static class Thresholds {

        private long empty;

        private long critical;

        private long low;

        private long replenish;
    }

    @Data
    public static class Message {

        private String empty;

        private String low;

        private String ok;
    }

}
