package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.mongo.GenreMongo;

import java.util.Optional;


public interface GenreRepository extends MongoRepository<GenreMongo, String> {
    Optional<GenreMongo> findByName(String name);
}
