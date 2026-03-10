package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.jpa.Author;
import ru.otus.hw.models.mongo.AuthorMongo;

@Component
public class AuthorConverter {

    public AuthorMongo fromJPAtoMongo(Author authorJPA) {
        AuthorMongo author = new AuthorMongo();
        author.setFullName(authorJPA.getFullName());
        return author;
    }
}
