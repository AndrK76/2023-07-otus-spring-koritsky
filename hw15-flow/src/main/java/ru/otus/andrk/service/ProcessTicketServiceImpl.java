package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.andrk.config.ProcessTicketServiceConfig;
import ru.otus.andrk.model.Ticket;
import ru.otus.andrk.model.TicketHistory;
import ru.otus.andrk.model.TicketStatus;
import ru.otus.andrk.model.TroubleLevel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static ru.otus.andrk.model.TicketStatus.DECIDED;
import static ru.otus.andrk.model.TicketStatus.POSTPONED;

@Service
@RequiredArgsConstructor
public class ProcessTicketServiceImpl implements ProcessTicketService {

    private final Random random = new Random();

    private final Logger log = LoggerFactory.getLogger("ProcessTicket");

    private final ProcessTicketServiceConfig config;

    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    @Override
    public Ticket handle(Ticket ticket) {
        delay(random.nextInt(config.getMaxProcessTimeMs()));
        var newStatus = getProcessResult(ticket.getLevel());
        ticket.setStatus(newStatus);
        ticket.getHistory().add(
                new TicketHistory(new Date(), ticket.getStatus(), newStatus)
        );
        log.info("Process ticket: {} from: {}, result: {}, try: {}",
                ticket.getId(),
                dateFormat.format(ticket.getDateEvent()), ticket.getStatus(),
                ticket.getHistory().size());
        return ticket;
    }

    private static void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private TicketStatus getProcessResult(TroubleLevel level) {
        var rndVal = random.nextInt(100);
        var sucBound = switch (level) {
            case HIGH -> config.getApplyHighPercent();
            case MEDIUM -> config.getApplyMediumPercent();
            case LOWER -> config.getApplyLowPercent();
        };
        return rndVal > sucBound ? POSTPONED : DECIDED;
    }
}
