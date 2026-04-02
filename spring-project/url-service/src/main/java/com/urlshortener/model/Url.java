package com.urlshortener.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @NotEmpty
    @Field(name = "shortUrl")
    private String shortUrl;

    @NotNull
    @NotEmpty
    @Field(name = "longUrl")
    private String longUrl;

    @NotNull
    @NotEmpty
    @Field(name = "userId")
    private Long userId;

    private Instant createdAt;
}
