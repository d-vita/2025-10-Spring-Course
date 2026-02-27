package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.jpa.Genre;
import ru.otus.hw.models.mongo.GenreMongo;

@Component
public class GenreConverter {

    public GenreMongo fromJPAtoMongo(Genre genre) {
        GenreMongo genreMongo = new GenreMongo();
        genreMongo.setName(genre.getName());
        return genreMongo;
    }

}
