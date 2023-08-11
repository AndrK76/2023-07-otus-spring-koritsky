package ru.otus.andrk.converters;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.model.Question;

@Component
public class QuestionToStringConverter implements Converter<Question, String> {

    @Override
    public String convert(Question question) {
        StringBuilder sb = new StringBuilder(question.getQueryText())
                .append("\n")
                .append("Answers:\n");
        question.getAnswers()
                .forEach((index, answer) -> sb
                        .append("\t")
                        .append(index)
                        .append(". ")
                        .append(answer.getAnswerText())
                        .append("\n"));
        sb.append(
                switch (question.getQueryType()) {
                    case ONE_VALID_ANSWER -> "Enter valid number:";
                    case MANY_VALID_ANSWERS -> "Enter all valid numbers via comma:";
                }
        );
        return sb.toString();
    }

}
