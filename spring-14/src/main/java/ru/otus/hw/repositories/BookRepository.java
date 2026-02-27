package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.mongo.BookMongo;

public interface BookRepository extends MongoRepository<BookMongo, String> {
}
