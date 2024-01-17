package ru.otus.andrk.service;

import ru.otus.andrk.model.DeviceTrouble;
import ru.otus.andrk.model.Ticket;

public interface RegistrationService {
    Ticket registerTrouble(DeviceTrouble trouble);
}
