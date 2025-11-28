package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.Application;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Application.class)
public class QuestionDaoIntegrationTest {

    @Autowired
    private QuestionDao questionDao;

    private TestService testService;

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();


    @Test
    void executeTestFor_ShouldReturnCorrectResults_WhenAskForQuestions() {
        setupServiceWithUserInput("1\\n3\\n");

        String userInput = "2\n1\n";
        setupServiceWithUserInput(userInput);

        Student student = new Student("Ivan", "Petrov");
        TestResult result = testService.executeTestFor(student);

        assertThat(result.getStudent()).isEqualTo(student);
        assertThat(result.getRightAnswersCount()).isEqualTo(1);
    }

    @Test
    void executeTestFor_ShouldPrintQuestionsAndAnswers() {
        String userInput = "2\n3\n";
        setupServiceWithUserInput(userInput);

        Student student = new Student("Ivan", "Petrov");
        testService.executeTestFor(student);

        String output = out.toString();

        assertThat(output).contains("What is 2 + 2?");
        assertThat(output).contains("What is the capital of France?");

        assertThat(output).contains("22");
        assertThat(output).contains("4");
        assertThat(output).contains("London");
        assertThat(output).contains("Madrid");
        assertThat(output).contains("Paris");
    }

    @Test
    void executeTestFor_ShouldPrintCorrectOutput_WhenAskForQuestionsWithWrongAnswers() {
        setupServiceWithUserInput("1\n1\n");

        Student student = new Student("Ivan", "Petrov");
        testService.executeTestFor(student);

        String output = out.toString();

        assertThat(output).contains("What is 2 + 2?");
        assertThat(output).contains("What is the capital of France?");

        assertThat(output).contains("22");
        assertThat(output).contains("4");
        assertThat(output).contains("London");
        assertThat(output).contains("Madrid");
        assertThat(output).contains("Paris");
    }

    private void setupServiceWithUserInput(String input) {
        this.testService = new TestServiceImpl(
                new StreamsIOService(
                        new PrintStream(out),
                        new ByteArrayInputStream(input.getBytes())
                ),
                questionDao
        );
    }
}
