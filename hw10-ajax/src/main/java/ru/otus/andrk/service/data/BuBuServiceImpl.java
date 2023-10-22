package ru.otus.andrk.service.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.andrk.config.ApplicationSettings;
import ru.otus.andrk.exception.BuBuException;

@Service
@RequiredArgsConstructor
public class BuBuServiceImpl implements BuBuService {

    private final ApplicationSettings appSettings;

    @Override
    public void tryBuBu() {
        if (appSettings.isEbableBuBu()) {
            if ((int) (Math.random() * appSettings.getBubuFactor()) == 0) {
                throw new BuBuException();
            }
        }
    }
}
