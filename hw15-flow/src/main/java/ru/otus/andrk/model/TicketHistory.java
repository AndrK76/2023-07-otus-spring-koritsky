package ru.otus.andrk.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
@Getter
public class TicketHistory {
    private final Date dateEvent;

    private final TicketStatus from;

    private final TicketStatus to;
}
