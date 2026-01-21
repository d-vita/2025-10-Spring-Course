package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeLog(order = "000")
public class MongoDbChangelogs {

    @ChangeSet(order = "001", id = "dropDB", author = "admin", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "002", id = "initData", author = "admin")
    public void initData(AuthorRepository authorRepository, GenreRepository genreRepository,
                     BookRepository bookRepository, CommentRepository commentRepository) {
        Author author1 = authorRepository.save(new Author("1", "Author_1"));
        Author author2 = authorRepository.save(new Author("2", "Author_2"));
        Author author3 = authorRepository.save(new Author("3", "Author_3"));

        Genre genre1 = genreRepository.save(new Genre("1", "Genre_1"));
        Genre genre2 = genreRepository.save(new Genre("2", "Genre_2"));
        Genre genre3 = genreRepository.save(new Genre("3", "Genre_3"));

        Book book1 = bookRepository.save(new Book("1", "BookTitle_1", author1.getId(), genre1.getId()));
        Book book2 = bookRepository.save(new Book("2", "BookTitle_2", author2.getId(), genre2.getId()));
        Book book3 = bookRepository.save(new Book("3", "BookTitle_3", author3.getId(), genre3.getId()));

        commentRepository.save(new Comment("1", "Comment1", book1.getId()));
        commentRepository.save(new Comment("2", "Comment2", book2.getId()));
        commentRepository.save(new Comment("3", "Comment3", book3.getId()));
    }
}
