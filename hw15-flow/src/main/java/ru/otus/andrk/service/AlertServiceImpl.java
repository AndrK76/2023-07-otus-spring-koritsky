package ru.otus.andrk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Ticket;
import ru.otus.andrk.model.TroubleLevel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Service
public class AlertServiceImpl implements AlertService {
    private final Logger log = LoggerFactory.getLogger("AlertSend");

    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    @Override
    public Ticket sendRegCriticalEventAlert(Ticket ticket) {
        if (ticket.getLevel() == TroubleLevel.HIGH) {
            log.warn("New Critical Problem Id: {}, From: {}, Caution: {}",
                    ticket.getId(), dateFormat.format(ticket.getDateEvent()), ticket.getDescription());
        }
        return ticket;
    }

    @Override
    public Ticket sendNoDecideEventAlert(Ticket ticket) {
        if (ticket.getLevel() == TroubleLevel.HIGH) {
            log.error("No Decided Critical Problem Id: {}, From: {},Try: {}, Caution: {}",
                    ticket.getId(), dateFormat.format(ticket.getDateEvent()),
                    ticket.getHistory().size(), ticket.getDescription());
        } else {
            log.warn("No Decided Problem Id: {}, From: {},Try: {}, Caution: {}",
                    ticket.getId(), dateFormat.format(ticket.getDateEvent()),
                    ticket.getHistory().size(), ticket.getDescription());
        }
        return ticket;
    }
}
