package com.urlshortener.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "urls")
public class Url {
    @Id
    private String id;

    @NotBlank
    @Field(name = "shortUrl")
    private String shortUrl;

    @NotBlank
    @Field(name = "longUrl")
    private String longUrl;

    @NotBlank
    @Field(name = "userId")
    private Long userId;

    private Instant createdAt;
}
