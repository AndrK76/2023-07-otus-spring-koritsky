package ru.otus.andrk.service.student;

import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Student;
import ru.otus.andrk.service.dialog.DialogService;
import ru.otus.andrk.service.i18n.MessageProvider;

@Service
public class StudentInfoServiceDialogImpl implements StudentInfoService {
    private final DialogService dialogService;

    private final MessageProvider messageProvider;

    public StudentInfoServiceDialogImpl(DialogService dialogService, MessageProvider messageProvider) {
        this.dialogService = dialogService;
        this.messageProvider = messageProvider;
    }

    @Override
    public Student getStudent() {
        dialogService.displayText(messageProvider.getMessage("ENTER_FIRST_NAME"));
        String firstName = dialogService.readText();
        dialogService.displayText(messageProvider.getMessage("ENTER_LAST_NAME"));
        String lastName = dialogService.readText();
        return new Student(firstName, lastName);
    }
}
