package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class Book {
    @Id
    private String id;

    private String title;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @DBRef(lazy = true)
    private Author author;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @DBRef(lazy = true)
    private Genre genre;
}
