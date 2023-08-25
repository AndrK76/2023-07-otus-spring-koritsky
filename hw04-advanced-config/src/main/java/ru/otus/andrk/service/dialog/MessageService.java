package ru.otus.andrk.service.dialog;

public interface MessageService {
    String getMessageAsText(Message message);

    String readText();

    void showMessage(Message message);

}
