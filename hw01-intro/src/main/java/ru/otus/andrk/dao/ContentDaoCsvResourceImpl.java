package ru.otus.andrk.dao;

import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.model.QuestionType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class ContentDaoCsvResourceImpl implements ContentDao {

    private static final String DELIMITER = ";";

    private final String resourceName;
    private final Map<Integer, Question> questions;

    public ContentDaoCsvResourceImpl(String resourceName) {
        this.resourceName = resourceName;
        questions = new HashMap<>();
    }

    @Override
    public List<Question> getQuestions() {
        if (questions.isEmpty()){
            loadFromCsv();
        }
        return questions.values().stream()
                .sorted(Comparator.comparing(Question::getNum))
                .collect(Collectors.toList());
    }

    private void loadFromCsv() {
        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                Objects.requireNonNull(
                                        getClass().getClassLoader().getResourceAsStream(resourceName))))) {
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line);
            }
        } catch (IOException e) {
            throw new ContentLoadException(e);
        }
    }

    void parseLine(String line) {
        String[] parts = line.split(DELIMITER);
        if (parts.length < 2) {
            throw new ContentLoadException("Invalid file format, incorrect string");
        }
        switch (parts[1].toUpperCase()) {
            case "Q" -> parseQuestion(parts);
            case "A" -> parseAnswer(parts);
            default -> throw new ContentLoadException("Invalid file format, incorrect string type");
        }
    }

    void parseQuestion(String[] srcData) {
        try {
            int queryIndex = Integer.parseInt(srcData[0]);
            String queryText = srcData[3];
            QuestionType queryType = QuestionType.byName(srcData[2]);
            if (questions.containsKey(queryIndex)) {
                throw new ContentLoadException("Invalid file format, duplicate question");
            }
            questions.put(queryIndex, Question.builder()
                    .num(queryIndex)
                    .queryText(queryText)
                    .queryType(queryType)
                    .build());
        } catch (ContentLoadException e) {
            throw e;
        } catch (Throwable e) {
            throw new ContentLoadException("Invalid format in question string", e);
        }
    }

    void parseAnswer(String[] srcData) {
        try {
            int queryIndex = Integer.parseInt(srcData[0]);
            String answerText = srcData[3];
            boolean isValidAnswer = srcData[2].equals("1");
            if (!questions.containsKey(queryIndex)) {
                throw new ContentLoadException("Invalid file format, no exist query for answer");
            }
            Question question = questions.get(queryIndex);
            question.getAnswers().add(Answer.builder()
                    .num(question.getAnswers().size() + 1)
                    .answerText(answerText)
                    .valid(isValidAnswer)
                    .build());
        } catch (ContentLoadException e) {
            throw e;
        } catch (Throwable e) {
            throw new ContentLoadException("Invalid format in question string", e);
        }
    }
}

