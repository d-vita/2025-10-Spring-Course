package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printLine("Please answer the questions below");

        printAllQuestions();
    }

    private void printAllQuestions() {
        for (Question question : questionDao.findAll()) {
            List<String> formattedQuestion = formatQuestion(question);
            for (String line : formattedQuestion) {
                ioService.printLine(line);
            }
        }
    }

    private List<String> formatQuestion(Question question) {
        List<String> lines = new ArrayList<>();
        lines.add(question.text());
        for (Answer answer : question.answers()) {
            lines.add(String.format("- %s", answer.text()));
        }
        return lines;
    }
}
