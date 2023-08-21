package ru.otus.andrk.dao;

import org.springframework.stereotype.Repository;
import ru.otus.andrk.config.QuestionsDaoCsvConfig;
import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.model.QuestionType;
import ru.otus.andrk.service.i18n.ResourceProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Repository
public class QuestionDaoCsvResource implements QuestionDao {

    private final QuestionsDaoCsvConfig csvConfig;

    private final ResourceProvider resourceProvider;


    public QuestionDaoCsvResource(
            QuestionsDaoCsvConfig csvConfig, ResourceProvider resourceProvider) {
        this.csvConfig = csvConfig;
        this.resourceProvider = resourceProvider;
    }

    @Override
    public List<Question> getQuestions() {
        return loadQuestionsFromCsv();
    }

    private List<Question> loadQuestionsFromCsv() {
        Map<Integer, Question> questions = new HashMap<>();
        try (InputStream srcStream =
                     getClass().getClassLoader()
                             .getResourceAsStream(
                                     resourceProvider.getResourcePath(
                                             csvConfig.getResourceFolder(),
                                             csvConfig.getResourceName()))) {
            if (isNull(srcStream)) {
                throw new ContentLoadException("Resource not found");
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(srcStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    parseLine(line, questions);
                }

            }
        } catch (IOException | NullPointerException e) {
            throw new ContentLoadException(e);
        }
        return questions.values().stream().toList();
    }

    void parseLine(String line, Map<Integer, Question> questions) {
        String[] parts = line.split(csvConfig.getCsvDelimiter());
        if (parts.length < 2) {
            throw new ContentLoadException("Invalid file format, incorrect string");
        }
        switch (parts[1].toUpperCase()) {
            case "Q" -> parseQuestion(parts, questions);
            case "A" -> parseAnswer(parts, questions);
            default -> throw new ContentLoadException("Invalid file format, incorrect string type");
        }
    }

    void parseQuestion(String[] srcData, Map<Integer, Question> questions) {
        try {
            int queryIndex = Integer.parseInt(srcData[0]);
            String queryText = srcData[3];
            QuestionType queryType = QuestionType.byName(srcData[2]);
            if (questions.containsKey(queryIndex)) {
                throw new ContentLoadException("Invalid file format, duplicate question");
            }
            questions.put(queryIndex, Question.builder()
                    .queryText(queryText)
                    .queryType(queryType)
                    .build());
        } catch (ContentLoadException e) {
            throw e;
        } catch (Throwable e) {
            throw new ContentLoadException("Invalid format in question string", e);
        }
    }

    void parseAnswer(String[] srcData, Map<Integer, Question> questions) {
        try {
            int queryIndex = Integer.parseInt(srcData[0]);
            String answerText = srcData[3];
            boolean isValidAnswer = srcData[2].equals("1");
            if (!questions.containsKey(queryIndex)) {
                throw new ContentLoadException("Invalid file format, no exist query for answer");
            }
            Question question = questions.get(queryIndex);
            question.getAnswers().add(new Answer(answerText, isValidAnswer));
        } catch (ContentLoadException e) {
            throw e;
        } catch (Throwable e) {
            throw new ContentLoadException("Invalid format in question string", e);
        }
    }
}

