package ru.otus.andrk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import ru.otus.andrk.service.dialogs.DialogService;
import ru.otus.andrk.service.students.StudentInfoService;
import ru.otus.andrk.service.students.StudentInfoServiceDialogImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Student info service dialog implementation unit test with mock")
public class StudentInfoServiceDialogImplTest {

    private DialogService dialogService;

    private StudentInfoService studentInfoService;

    @BeforeEach
    void initServices(){
        dialogService = mock(DialogService.class);
        when(dialogService.readText()).thenReturn("first").thenReturn("last");
        studentInfoService = new StudentInfoServiceDialogImpl(dialogService);
    }


    @Test
    public void shouldCallDialogServiceMethodsInExpectingOrder() {
        studentInfoService.getUserName();
        verify(dialogService,times(2)).readText();
        verify(dialogService,times(2)).displayText(anyString());
        InOrder inOrder = inOrder(dialogService);
        inOrder.verify(dialogService).displayText(anyString());
        inOrder.verify(dialogService).readText();
        inOrder.verify(dialogService).displayText(anyString());
        inOrder.verify(dialogService).readText();
    }

    @Test
    public void shouldReturnExpectedValue(){
        var student = studentInfoService.getUserName();
        assertThat(student.firstName()).isEqualTo("first");
        assertThat(student.lastName()).isEqualTo("last");
    }
}
