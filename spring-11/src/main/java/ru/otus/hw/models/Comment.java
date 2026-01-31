package ru.otus.hw.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    @NotNull
    @NotEmpty
    @Field(name = "message")
    private String message;

    @NotNull
    @Field(name = "book")
    private Book book;
}