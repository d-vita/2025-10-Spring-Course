package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.mongo.AuthorMongo;

import java.util.Optional;


public interface AuthorRepository extends MongoRepository<AuthorMongo, String> {
    Optional<AuthorMongo> findByFullName(String fullName);
}
