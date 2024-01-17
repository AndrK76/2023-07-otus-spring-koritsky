package ru.otus.andrk.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.otus.andrk.model.TicketStatus.NEW;

@Getter
public class Ticket {
    private final long id;

    private final Device device;

    private final TroubleLevel level;

    private final String description;

    private final Date dateEvent;

    private final Date dateRegistration;

    @Setter
    private TicketStatus status;

    private final List<TicketHistory> history;

    public Ticket(DeviceTrouble trouble, long ticketId) {
        this.id = ticketId;
        this.device = trouble.getDevice();
        this.level = trouble.getLevel();
        this.description = trouble.getDescription();
        this.dateEvent = trouble.getDate();
        this.dateRegistration = new Date();
        this.status = NEW;
        this.history = new ArrayList<>();
    }
}
