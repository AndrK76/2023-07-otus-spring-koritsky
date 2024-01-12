package ru.otus.andrk.service;

import org.springframework.stereotype.Service;
import ru.otus.andrk.config.TroubleSourceConfig;
import ru.otus.andrk.model.Device;
import ru.otus.andrk.model.DeviceTrouble;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Collections.unmodifiableList;
import static ru.otus.andrk.model.TroubleLevel.troubleByLevel;

@Service
public class TroubleSourceImpl implements TroubleSource {

    private static final int MAX_TROUBLE_LEVEL = 12;

    private final TroubleSourceConfig config;

    private final List<Device> devices;

    private final RegistrationTroubleGateway registrationGateway;

    private final Random random = new Random();

    public TroubleSourceImpl(TroubleSourceConfig config, RegistrationTroubleGateway registrationGateway) {
        this.config = config;
        List<Device> devices = new ArrayList<>();
        for (int i = 0; i < config.getDeviceCount(); i++) {
            devices.add(new Device(UUID.randomUUID(), String.format("device %d", i + 1)));
        }
        this.devices = unmodifiableList(devices);
        this.registrationGateway = registrationGateway;
    }

    @Override
    public void startGenerateTroubles() {
        var executor = Executors.newScheduledThreadPool(1);
        AtomicLong counter = new AtomicLong(0L);
        executor.scheduleAtFixedRate(() ->
                        registrationGateway.process(generateTroubles(counter.incrementAndGet())),
                0, config.getGenerationPeriodMs(), TimeUnit.MILLISECONDS);
    }

    private Collection<DeviceTrouble> generateTroubles(long partitionNumber) {
        List<DeviceTrouble> troubles = new ArrayList<>();
        var countTroubles = random.nextInt(config.getMaxPartitionSize());
        for (int i = 0; i < countTroubles; i++) {
            var device = devices.get(random.nextInt(config.getDeviceCount()));
            var level = troubleByLevel(random.nextInt(MAX_TROUBLE_LEVEL));
            troubles.add(new DeviceTrouble(device, level,
                    String.format("Trouble Part: %d, Num: %d", partitionNumber, i)));
        }
        return troubles;
    }


}
