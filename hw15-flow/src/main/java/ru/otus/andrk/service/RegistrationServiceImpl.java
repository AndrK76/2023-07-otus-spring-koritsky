package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.andrk.config.RegisterServiceConfig;
import ru.otus.andrk.model.DeviceTrouble;
import ru.otus.andrk.model.Ticket;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final Logger log = LoggerFactory.getLogger("Registration");

    private final AtomicLong registrar = new AtomicLong(0L);

    private final RegisterServiceConfig config;

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    @Override
    public Ticket registerTrouble(DeviceTrouble trouble) {
        try {
            var ticketFuture = executor.schedule(() -> {
                var ret = new Ticket(trouble, registrar.incrementAndGet());
                log.warn("Ticket {} registered at {}", ret.getId(), dateFormat.format(ret.getDateRegistration()));
                return ret;
            }, config.getRegisterTimeoutMs(), TimeUnit.MILLISECONDS);
            return ticketFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


}
