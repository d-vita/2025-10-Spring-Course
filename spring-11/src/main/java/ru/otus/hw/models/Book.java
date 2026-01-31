package ru.otus.hw.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Document(collection = "books")
public class Book {
    @Id
    private String id;

    @NotNull
    @NotEmpty
    @Field(name = "title")
    private String title;

    @NotNull
    @Field(name = "genre")
    private Genre genre;

    @NotNull
    @Field(name = "author")
    private Author author;
}
