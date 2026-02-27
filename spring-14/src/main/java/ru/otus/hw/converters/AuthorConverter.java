package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.jpa.Author;
import ru.otus.hw.models.mongo.AuthorMongo;

@Component
public class AuthorConverter {

    public AuthorMongo fromJPAtoMongo(Author author) {
        AuthorMongo mongoAuthor = new AuthorMongo();
        mongoAuthor.setFullName(author.getFullName());
        return mongoAuthor;
    }
}
