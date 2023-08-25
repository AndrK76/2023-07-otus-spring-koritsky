package ru.otus.andrk.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.andrk.model.Student;
import ru.otus.andrk.service.TesterService;
import ru.otus.andrk.service.auth.StudentLoginService;
import ru.otus.andrk.service.dialog.WelcomeService;

@ShellComponent
@RequiredArgsConstructor
public class TestSystemCommands {

    private final StudentLoginService studentLoginService;

    private final WelcomeService welcomeService;

    private final TesterService testerService;

    private Student student;

    @ShellMethod(value = "Login to test", key = {"login", "l"})
    public String login() {
        student = studentLoginService.getStudent();
        return welcomeService.getWelcomeMessage(student);
    }

    @ShellMethod(value = "Start test", key = {"start", "test", "s", "t"})
    @ShellMethodAvailability("isPublishEventCommandAvailable")
    public String doTest() {
        return testerService.runTestForStudent(student);
    }

    private Availability isPublishEventCommandAvailable() {
        return student == null
                ? Availability.unavailable(welcomeService.getNeedAuthMessage())
                : Availability.available();
    }


}
