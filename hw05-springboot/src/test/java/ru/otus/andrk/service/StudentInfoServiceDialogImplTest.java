package ru.otus.andrk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import ru.otus.andrk.service.dialog.DialogService;
import ru.otus.andrk.service.i18n.MessageProvider;
import ru.otus.andrk.service.student.StudentInfoService;
import ru.otus.andrk.service.student.StudentInfoServiceDialogImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@DisplayName("Student info service dialog implementation unit test with mock")
public class StudentInfoServiceDialogImplTest {

    private DialogService dialogService;

    private MessageProvider messageProvider;

    private StudentInfoService studentInfoService;

    @BeforeEach
    void initServices() {
        dialogService = mock(DialogService.class);
        messageProvider = mock(MessageProvider.class);


        when(dialogService.readText()).thenReturn("first").thenReturn("last");
        when(messageProvider.getMessage(any())).thenReturn("");
        studentInfoService = new StudentInfoServiceDialogImpl(dialogService, messageProvider);
    }


    @Test
    public void shouldCallDialogServiceMethodsInExpectingOrder() {
        studentInfoService.getStudent();
        verify(dialogService, times(2)).readText();
        verify(dialogService, times(2)).displayText(anyString());
        InOrder inOrder = inOrder(dialogService);
        inOrder.verify(dialogService).displayText(anyString());
        inOrder.verify(dialogService).readText();
        inOrder.verify(dialogService).displayText(anyString());
        inOrder.verify(dialogService).readText();
    }

    @Test
    public void shouldReturnExpectedValue() {
        var student = studentInfoService.getStudent();
        assertThat(student.firstName()).isEqualTo("first");
        assertThat(student.lastName()).isEqualTo("last");
    }
}
