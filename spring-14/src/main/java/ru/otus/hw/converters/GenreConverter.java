package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.jpa.Genre;
import ru.otus.hw.models.mongo.GenreMongo;

@Component
public class GenreConverter {

    public GenreMongo fromJPAtoMongo(Genre genreJPA) {
        GenreMongo genre = new GenreMongo();
        genre.setName(genreJPA.getName());
        return genre;
    }

}
