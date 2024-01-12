package ru.otus.andrk.model;

import lombok.Getter;

import java.util.Date;

@Getter
public class Ticket {
    private final long id;

    private final Device device;

    private final TroubleLevel level;

    private final String description;

    private final Date dateEvent;

    private final Date dateRegistration;

    private boolean closed;

    public Ticket(DeviceTrouble trouble, long ticketId) {
        this.id = ticketId;
        this.device = trouble.getDevice();
        this.level = trouble.getLevel();
        this.description = trouble.getDescription();
        this.dateEvent = trouble.getDate();
        this.dateRegistration = new Date();
        this.closed = false;
    }
}
