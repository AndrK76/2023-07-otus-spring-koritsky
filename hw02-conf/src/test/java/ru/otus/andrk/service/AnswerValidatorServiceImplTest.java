package ru.otus.andrk.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.service.questions.AnswerValidatorService;
import ru.otus.andrk.service.questions.AnswerValidatorServiceImpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerValidatorServiceImplTest {

    @ParameterizedTest
    @MethodSource("getTestParams")
    public void givenAnswersShouldReturnExceptedValue(Question question, List<Answer> answers, boolean expectedResult) {
        AnswerValidatorService answerValidatorService = new AnswerValidatorServiceImpl();
        var actualResult = answerValidatorService.checkAnswer(question, answers);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private static Stream<? extends Arguments> getTestParams() {
        TestQuestionsSource questionSource = new TestQuestionsSource();

        return Stream.of(
                Arguments.of(
                        questionSource.getQuestionOneValidTypeWithOneValidAnswer(),
                        getQueryAnswerByIdx(
                                questionSource.getQuestionOneValidTypeWithOneValidAnswer(), Stream.of(0)),
                        true),
                Arguments.of(questionSource.getQuestionOneValidTypeWithOneValidAnswer(),
                        getQueryAnswerByIdx(
                                questionSource.getQuestionOneValidTypeWithOneValidAnswer(), Stream.of(1)),
                        false),
                Arguments.of(questionSource.getQuestionOneValidTypeWithOneValidAnswer(),
                        getQueryAnswerByIdx(
                                questionSource.getQuestionOneValidTypeWithOneValidAnswer(), Stream.of(0, 1)),
                        false),
                Arguments.of(questionSource.getQuestionManyValidTypeWithTwoValidAnswer(),
                        getQueryAnswerByIdx(
                                questionSource.getQuestionManyValidTypeWithTwoValidAnswer(), Stream.of(0)),
                        false),
                Arguments.of(questionSource.getQuestionManyValidTypeWithTwoValidAnswer(),
                        getQueryAnswerByIdx(
                                questionSource.getQuestionManyValidTypeWithTwoValidAnswer(), Stream.of(1)),
                        false),
                Arguments.of(questionSource.getQuestionManyValidTypeWithTwoValidAnswer(),
                        getQueryAnswerByIdx(
                                questionSource.getQuestionManyValidTypeWithTwoValidAnswer(), Stream.of(0, 1)),
                        false),
                Arguments.of(questionSource.getQuestionManyValidTypeWithTwoValidAnswer(),
                        getQueryAnswerByIdx(
                                questionSource.getQuestionManyValidTypeWithTwoValidAnswer(), Stream.of(0, 2)),
                        true),
                Arguments.of(questionSource.getQuestionOneValidTypeWithTwoValidAnswer(),
                        getQueryAnswerByIdx(
                                questionSource.getQuestionManyValidTypeWithTwoValidAnswer(), Stream.of(1, 2)),
                        false),
                Arguments.of(questionSource.getQuestionOneValidTypeWithTwoValidAnswer(),
                        getQueryAnswerByIdx(
                                questionSource.getQuestionOneValidTypeWithTwoValidAnswer(), Stream.of(0, 2)),
                        false),
                Arguments.of(questionSource.getQuestionOneValidTypeWithTwoValidAnswer(),
                        getQueryAnswerByIdx(
                                questionSource.getQuestionOneValidTypeWithTwoValidAnswer(), Stream.of(0)),
                        true),
                Arguments.of(questionSource.getQuestionOneValidTypeWithTwoValidAnswer(),
                        getQueryAnswerByIdx(
                                questionSource.getQuestionOneValidTypeWithTwoValidAnswer(), Stream.of(2)),
                        true),
                Arguments.of(questionSource.getQuestionOneValidTypeWithTwoValidAnswer(),
                        getQueryAnswerByIdx(
                                questionSource.getQuestionOneValidTypeWithTwoValidAnswer(), Stream.of(1)),
                        false)
        );
    }

    private static List<Answer> getQueryAnswerByIdx(Question question, Stream<Integer> indexes) {
        return indexes.map(idx ->
                question.getAnswers().get(idx)
        ).collect(Collectors.toList());
    }


}



