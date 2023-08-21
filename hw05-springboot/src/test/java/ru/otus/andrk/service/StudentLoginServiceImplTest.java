package ru.otus.andrk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import ru.otus.andrk.service.auth.StudentLoginService;
import ru.otus.andrk.service.auth.StudentLoginServiceImpl;
import ru.otus.andrk.service.i18n.MessageProvider;
import ru.otus.andrk.service.io.IOService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@DisplayName("Student login service implementation unit test with mock")
public class StudentLoginServiceImplTest {

    private IOService ioService;

    private MessageProvider messageProvider;

    private StudentLoginService studentLoginService;

    @BeforeEach
    void initIoService() {
        ioService = mock(IOService.class);
        when(ioService.readText()).thenReturn("first").thenReturn("last");
    }

    @BeforeEach
    void initMessageProvider() {
        messageProvider = mock(MessageProvider.class);
        when(messageProvider.getMessage(any())).thenReturn("");
    }


    @Test
    public void shouldCallDialogServiceMethodsInExpectingOrder() {
        studentLoginService = new StudentLoginServiceImpl(ioService, messageProvider);
        studentLoginService.getStudent();
        verify(ioService, times(2)).readText();
        verify(ioService, times(2)).displayText(anyString());
        InOrder inOrder = inOrder(ioService);
        inOrder.verify(ioService).displayText(anyString());
        inOrder.verify(ioService).readText();
        inOrder.verify(ioService).displayText(anyString());
        inOrder.verify(ioService).readText();
    }

    @Test
    public void shouldReturnExpectedValue() {
        studentLoginService = new StudentLoginServiceImpl(ioService, messageProvider);
        var student = studentLoginService.getStudent();
        assertThat(student.firstName()).isEqualTo("first");
        assertThat(student.lastName()).isEqualTo("last");
    }
}
