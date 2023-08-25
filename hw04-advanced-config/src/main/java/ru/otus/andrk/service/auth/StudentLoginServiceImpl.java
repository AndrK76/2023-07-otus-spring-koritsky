package ru.otus.andrk.service.auth;

import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Student;
import ru.otus.andrk.service.dialog.Message;
import ru.otus.andrk.service.dialog.MessageService;

@Service
public class StudentLoginServiceImpl implements StudentLoginService {

    private final MessageService messageService;

    public StudentLoginServiceImpl(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public Student getStudent() {
        messageService.showMessage(new Message("ENTER_FIRST_NAME"));
        String firstName = messageService.readText();
        messageService.showMessage(new Message("ENTER_LAST_NAME"));
        String lastName = messageService.readText();
        return new Student(firstName, lastName);
    }
}
