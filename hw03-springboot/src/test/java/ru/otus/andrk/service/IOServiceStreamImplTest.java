package ru.otus.andrk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.andrk.service.io.IOService;
import ru.otus.andrk.service.io.IOServiceStreamImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Service for dialog with user (no mock)")
public class IOServiceStreamImplTest {

    private static final String NL_SEPARATOR = System.lineSeparator();

    private IOService ioService;

    private ByteArrayOutputStream out;

    private ByteArrayInputStream in;


    @BeforeEach
    public void initData() {
        out = new ByteArrayOutputStream();
        in = new ByteArrayInputStream(new byte[]{});
        ioService = new IOServiceStreamImpl(out, in, Charset.defaultCharset());
    }

    @ParameterizedTest
    @MethodSource("getSampleData")
    public void givenTextShouldBeWriteToOutStreamWithNewLineSeparator(String srcText) {
        String expected = srcText + NL_SEPARATOR;
        ioService.displayText(srcText);
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
