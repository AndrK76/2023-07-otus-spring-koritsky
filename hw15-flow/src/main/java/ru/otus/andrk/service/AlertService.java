package ru.otus.andrk.service;

import ru.otus.andrk.model.Ticket;

public interface AlertService {
    Ticket sendRegCriticalEventAlert(Ticket ticket);

    Ticket sendNoDecideEventAlert(Ticket ticket);
}
