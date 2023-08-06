package ru.otus.andrk.service.students;

import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Student;
import ru.otus.andrk.service.dialogs.DialogService;

@Service
public class StudentInfoServiceDialogImpl implements StudentInfoService {
    private final DialogService dialogService;

    public StudentInfoServiceDialogImpl(DialogService dialogService) {
        this.dialogService = dialogService;
    }

    @Override
    public Student getUserName() {
        dialogService.displayText("Enter First Name:");
        String firstName = dialogService.readText();
        dialogService.displayText("Enter Last Name:");
        String lastName = dialogService.readText();
        return new Student(firstName,lastName);
    }
}
