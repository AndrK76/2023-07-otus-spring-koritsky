package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.andrk.model.DeviceTrouble;
import ru.otus.andrk.model.Ticket;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final Logger log = LoggerFactory.getLogger("Registration");

    private final AtomicLong registrar = new AtomicLong(0L);

    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    @Override
    public Ticket registerTrouble(DeviceTrouble trouble) {
        var ret = new Ticket(trouble, registrar.incrementAndGet());
        log.info("Ticket {} registered at {}", ret.getId(), dateFormat.format(ret.getDateRegistration()));
        return ret;
    }


}
