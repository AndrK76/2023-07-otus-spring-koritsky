package ru.otus.andrk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Service for dialog with user")
public class DialogServiceStreamImplTest {

    private static final String NL_SEPARATOR = System.lineSeparator();

    private DialogService dialogService;

    private ByteArrayOutputStream out;


    @BeforeEach
    public void initData() {
        out = new ByteArrayOutputStream();
        dialogService = new DialogServiceStreamImpl(out);
    }

    @ParameterizedTest
    @MethodSource("getSampleData")
    public void givenTextShouldBeWriteToOutStreamWithNewLineSeparator(String srcText) {
        String expected = srcText + NL_SEPARATOR;
        dialogService.displayText(srcText);
        var strResult = out.toString();
        assertThat(strResult)
                .isNotNull()
                .isEqualTo(expected);
    }

    private static List<String> getSampleData() {
        return List.of(
                "simple text",
                "text" + NL_SEPARATOR + "contain new line separator",
                "text ends with new line separator" + NL_SEPARATOR,
                NL_SEPARATOR + "text start with new line separator"
        );
    }

}
