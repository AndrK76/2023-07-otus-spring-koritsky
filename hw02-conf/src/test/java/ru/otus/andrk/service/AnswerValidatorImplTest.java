package ru.otus.andrk.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.service.questions.AnswerValidator;
import ru.otus.andrk.service.questions.AnswerValidatorImpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerValidatorImplTest {


    @ParameterizedTest
    @MethodSource("getTestParams")
    public void givenAnswersShouldReturnExceptedValue(Question question, List<Answer> answers, boolean expectedResult) {
        AnswerValidator answerValidator = new AnswerValidatorImpl();
        var actualResult = answerValidator.checkAnswer(question, answers);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private static Stream<? extends Arguments> getTestParams() {
        TestQuestionsSource questionSource = new TestQuestionsSource();

        return Stream.of(
                Arguments.of(questionSource.getQuestionOneValidTypeWithOneValidAnswer(),
                        questionSource.getQuestionOneValidTypeWithOneValidAnswer().getAnswers()
                                .entrySet().stream().filter(r -> r.getKey() == 1)
                                .map(Map.Entry::getValue).collect(Collectors.toList()),
                        true),
                Arguments.of(questionSource.getQuestionOneValidTypeWithOneValidAnswer(),
                        questionSource.getQuestionOneValidTypeWithOneValidAnswer().getAnswers()
                                .entrySet().stream().filter(r -> r.getKey() == 2)
                                .map(Map.Entry::getValue).collect(Collectors.toList()),
                        false),
                Arguments.of(questionSource.getQuestionOneValidTypeWithOneValidAnswer(),
                        questionSource.getQuestionOneValidTypeWithOneValidAnswer().getAnswers()
                                .entrySet().stream().filter(r -> List.of(1,2).contains(r.getKey()))
                                .map(Map.Entry::getValue).collect(Collectors.toList()),
                        false),
                Arguments.of(questionSource.getQuestionManyValidTypeWithTwoValidAnswer(),
                        questionSource.getQuestionManyValidTypeWithTwoValidAnswer().getAnswers()
                                .entrySet().stream().filter(r -> r.getKey() == 1)
                                .map(Map.Entry::getValue).collect(Collectors.toList()),
                        false),
                Arguments.of(questionSource.getQuestionManyValidTypeWithTwoValidAnswer(),
                        questionSource.getQuestionManyValidTypeWithTwoValidAnswer().getAnswers()
                                .entrySet().stream().filter(r -> r.getKey() == 2)
                                .map(Map.Entry::getValue).collect(Collectors.toList()),
                        false),
                Arguments.of(questionSource.getQuestionManyValidTypeWithTwoValidAnswer(),
                        questionSource.getQuestionManyValidTypeWithTwoValidAnswer().getAnswers()
                                .entrySet().stream().filter(r -> List.of(1,2).contains(r.getKey()))
                                .map(Map.Entry::getValue).collect(Collectors.toList()),
                        false),
                Arguments.of(questionSource.getQuestionManyValidTypeWithTwoValidAnswer(),
                        questionSource.getQuestionManyValidTypeWithTwoValidAnswer().getAnswers()
                                .entrySet().stream().filter(r -> List.of(1,3).contains(r.getKey()))
                                .map(Map.Entry::getValue).collect(Collectors.toList()),
                        true),
                Arguments.of(questionSource.getQuestionOneValidTypeWithTwoValidAnswer(),
                        questionSource.getQuestionOneValidTypeWithTwoValidAnswer().getAnswers()
                                .entrySet().stream().filter(r -> List.of(1,2).contains(r.getKey()))
                                .map(Map.Entry::getValue).collect(Collectors.toList()),
                        false),
                Arguments.of(questionSource.getQuestionOneValidTypeWithTwoValidAnswer(),
                        questionSource.getQuestionOneValidTypeWithTwoValidAnswer().getAnswers()
                                .entrySet().stream().filter(r -> List.of(1,3).contains(r.getKey()))
                                .map(Map.Entry::getValue).collect(Collectors.toList()),
                        false),
                Arguments.of(questionSource.getQuestionOneValidTypeWithTwoValidAnswer(),
                        questionSource.getQuestionOneValidTypeWithTwoValidAnswer().getAnswers()
                                .entrySet().stream().filter(r -> r.getKey() == 1)
                                .map(Map.Entry::getValue).collect(Collectors.toList()),
                        true),
                Arguments.of(questionSource.getQuestionOneValidTypeWithTwoValidAnswer(),
                        questionSource.getQuestionOneValidTypeWithTwoValidAnswer().getAnswers()
                                .entrySet().stream().filter(r -> r.getKey() == 3)
                                .map(Map.Entry::getValue).collect(Collectors.toList()),
                        true),
                Arguments.of(questionSource.getQuestionOneValidTypeWithTwoValidAnswer(),
                        questionSource.getQuestionOneValidTypeWithTwoValidAnswer().getAnswers()
                                .entrySet().stream().filter(r -> r.getKey() == 2)
                                .map(Map.Entry::getValue).collect(Collectors.toList()),
                        false)
        );
    }

}



