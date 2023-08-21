package ru.otus.andrk.service.auth;

import org.springframework.stereotype.Component;
import ru.otus.andrk.model.Student;
import ru.otus.andrk.service.i18n.MessageProvider;
import ru.otus.andrk.service.io.IOService;

@Component
public class StudentLoginServiceImpl implements StudentLoginService {

    private final IOService ioService;

    private final MessageProvider messageProvider;

    public StudentLoginServiceImpl(IOService ioService, MessageProvider messageProvider) {
        this.ioService = ioService;
        this.messageProvider = messageProvider;
    }

    @Override
    public Student getStudent() {
        ioService.displayText(messageProvider.getMessage("ENTER_FIRST_NAME"));
        String firstName = ioService.readText();
        ioService.displayText(messageProvider.getMessage("ENTER_LAST_NAME"));
        String lastName = ioService.readText();
        return new Student(firstName, lastName);
    }
}
