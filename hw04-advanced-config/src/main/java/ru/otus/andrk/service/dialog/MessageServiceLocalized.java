package ru.otus.andrk.service.dialog;

import org.springframework.stereotype.Service;
import ru.otus.andrk.i18n.MessageProvider;
import ru.otus.andrk.service.io.IOService;

import java.util.stream.Collectors;

@Service
public class MessageServiceLocalized implements MessageService {

    private final IOService ioService;

    private final MessageProvider messageProvider;

    public MessageServiceLocalized(IOService ioService, MessageProvider messageProvider) {
        this.ioService = ioService;
        this.messageProvider = messageProvider;
    }

    @Override
    public String getMessageAsText(Message message) {
        return composeMessage(message);
    }

    @Override
    public String readText() {
        return ioService.readText();
    }

    @Override
    public void showMessage(Message message) {
        ioService.displayText(composeMessage(message));
    }

    private String composeMessage(Message message) {
        return message.getParts().stream()
                .map(this::localizeMessagePart)
                .collect(Collectors.joining());
    }

    private String localizeMessagePart(Message.MessagePart messagePart) {
        if (messagePart.messageKey() == null) {
            return (String) messagePart.messageArgs()[0];
        } else {
            return messageProvider.getMessage(messagePart.messageKey(), messagePart.messageArgs());
        }
    }

}
