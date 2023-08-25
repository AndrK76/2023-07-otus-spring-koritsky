package ru.otus.andrk.service.dialog;

import ru.otus.andrk.model.Student;

public interface WelcomeService {
    String getWelcomeMessage(Student student);

    String getNeedAuthMessage();
}
