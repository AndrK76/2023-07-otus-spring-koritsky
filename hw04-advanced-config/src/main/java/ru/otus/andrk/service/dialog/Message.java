package ru.otus.andrk.service.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Message {

    private List<MessagePart> parts = new ArrayList<>();

    public Message(String messageKey, Object... args) {
        parts.add(new MessagePart(messageKey, args));
    }

    private Message() {

    }

    public List<MessagePart> getParts() {
        return Collections.unmodifiableList(parts);
    }

    public static Builder builder() {
        return new Message().new Builder();
    }

    public record MessagePart(String messageKey, Object[] messageArgs) {

    }

    public class Builder {
        private Builder() {

        }

        public Builder addMessage(String messageKey, String... args) {
            Message.this.parts.add(new MessagePart(messageKey, args));
            return this;
        }

        public Builder addText(String text) {
            Message.this.parts.add(new MessagePart(null, new String[]{text}));
            return this;
        }

        public Message build() {
            return Message.this;
        }
    }


}
