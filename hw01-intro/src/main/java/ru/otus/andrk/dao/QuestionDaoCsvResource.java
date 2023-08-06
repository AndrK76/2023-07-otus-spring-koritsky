package ru.otus.andrk.dao;

import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.model.QuestionType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionDaoCsvResource implements QuestionDao {

    private static final String DELIMITER = ";";

    private final String resourceName;


    public QuestionDaoCsvResource(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public List<Question> getQuestions() {
        return loadFromCsv().values().stream().toList();
    }

    private Map<Integer, Question> loadFromCsv() {
        Map<Integer, Question> questions = new HashMap<>();
        try (InputStream srcStream =
                     getClass().getClassLoader().getResourceAsStream(resourceName);
             BufferedReader br = new BufferedReader(new InputStreamReader(srcStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line, questions);
            }
        } catch (IOException | NullPointerException e) {
            throw new ContentLoadException(e);
        }
        return questions;
    }

    void parseLine(String line, Map<Integer, Question> questions) {
        String[] parts = line.split(DELIMITER);
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

