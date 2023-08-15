package ru.otus.andrk.service.student;

import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Student;
import ru.otus.andrk.service.dialog.DialogService;
import ru.otus.andrk.service.i18n.MessageService;

@Service
public class StudentInfoServiceDialogImpl implements StudentInfoService {
    private final DialogService dialogService;

    private final MessageService messageService;

    public StudentInfoServiceDialogImpl(DialogService dialogService, MessageService messageService) {
        this.dialogService = dialogService;
        this.messageService = messageService;
    }

    @Override
    public Student getStudent() {
        dialogService.displayText(messageService.getMessage("ENTER_FIRST_NAME"));
        String firstName = dialogService.readText();
        dialogService.displayText(messageService.getMessage("ENTER_LAST_NAME"));
        String lastName = dialogService.readText();
        return new Student(firstName, lastName);
    }
}
