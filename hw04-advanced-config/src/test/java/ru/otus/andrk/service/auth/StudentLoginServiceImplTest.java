package ru.otus.andrk.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.andrk.service.dialog.MessageService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@ContextConfiguration(classes = StudentLoginServiceImpl.class)
public class StudentLoginServiceImplTest {

    @MockBean
    private MessageService messageService;

    @Autowired
    private StudentLoginService studentLoginService;

    @BeforeEach
    void initMocks() {
        when(messageService.readText())
                .thenReturn("first")
                .thenReturn("last");
    }

    @Test
    public void shouldCallDialogServiceMethodsInExpectingOrder() {
        studentLoginService.getStudent();
        verify(messageService, times(2)).readText();
        verify(messageService, times(2)).showMessage(any());
        InOrder inOrder = inOrder(messageService);
        inOrder.verify(messageService).showMessage(any());
        inOrder.verify(messageService).readText();
        inOrder.verify(messageService).showMessage(any());
        inOrder.verify(messageService).readText();
    }

    @Test
    public void shouldReturnExpectedValue() {
        var student = studentLoginService.getStudent();
        assertThat(student.firstName()).isEqualTo("first");
        assertThat(student.lastName()).isEqualTo("last");
    }
}
