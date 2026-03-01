package ru.otus.hw.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.models.mongo.GenreMongo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
@Import({AuthorBatchConfig.class, GenreBatchConfig.class, BookBatchConfig.class})
@Sql("/data.sql")
public class BookBatchConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private Job importAuthorJob;

    @Autowired
    private Job importGenreJob;

    @Autowired
    private Job importBookJob;

    @BeforeEach
    void setUp() throws Exception {
        mongoTemplate.dropCollection("authors");
        mongoTemplate.dropCollection("genres");
        mongoTemplate.dropCollection("books");

        jobRepositoryTestUtils.removeJobExecutions();

        jobLauncherTestUtils.setJob(importAuthorJob);
        jobLauncherTestUtils.launchJob();

        jobLauncherTestUtils.setJob(importGenreJob);
        jobLauncherTestUtils.launchJob();

        jobLauncherTestUtils.setJob(importBookJob);
    }

    @Test
    void testImportBookJob() throws Exception {
        JobExecution execution = jobLauncherTestUtils.launchJob();

        assertThat(execution.getExitStatus())
                .isEqualTo(ExitStatus.COMPLETED);

        List<AuthorMongo> authors = mongoTemplate.findAll(AuthorMongo.class, "authors");
        assertThat(authors)
                .hasSize(3)
                .extracting(AuthorMongo::getFullName)
                .containsExactlyInAnyOrder("Author_1", "Author_2", "Author_3");

        List<GenreMongo> genres = mongoTemplate.findAll(GenreMongo.class, "genres");
        assertThat(genres)
                .hasSize(3)
                .extracting(GenreMongo::getName)
                .containsExactlyInAnyOrder("Genre_1", "Genre_2", "Genre_3");

        List<BookMongo> books = mongoTemplate.findAll(BookMongo.class, "books");
        assertThat(books)
                .hasSize(3)
                .extracting(BookMongo::getTitle)
                .containsExactlyInAnyOrder("BookTitle_1", "BookTitle_2", "BookTitle_3");
    }
}
