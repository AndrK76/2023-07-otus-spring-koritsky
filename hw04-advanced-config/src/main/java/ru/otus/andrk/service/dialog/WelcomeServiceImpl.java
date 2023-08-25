package ru.otus.andrk.service.dialog;

import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Student;

@Service
public class WelcomeServiceImpl implements WelcomeService {

    private final MessageService messageService;

    public WelcomeServiceImpl(MessageService messageService) {
        this.messageService = messageService;
    }


    @Override
    public String getWelcomeMessage(Student student) {
        return messageService.getMessageAsText(
                new Message("HELLO_MESSAGE", student.firstName(), student.lastName()));
    }

    @Override
    public String getNeedAuthMessage() {
        return messageService.getMessageAsText(new Message("NEED_AUTH"));
    }
}
