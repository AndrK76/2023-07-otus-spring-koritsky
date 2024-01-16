package ru.otus.andrk.service;

import ru.otus.andrk.model.Ticket;

public interface ProcessTicketService {
    Ticket handle(Ticket ticket);
}
