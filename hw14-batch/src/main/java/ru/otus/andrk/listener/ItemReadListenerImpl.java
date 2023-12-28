package ru.otus.andrk.listener;

import org.slf4j.Logger;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.lang.NonNull;

public class ItemReadListenerImpl<T> implements ItemReadListener<T> {

    private final Logger logger;

    public ItemReadListenerImpl(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void beforeRead() {
        logger.info("Начало чтения");
    }

    @Override
    public void afterRead(T object) {
        logger.info("Конец чтения {}",object.getClass().getSimpleName());
    }

    @Override
    public void onReadError(@NonNull Exception e) {
        logger.info("Ошибка чтения");
    }
}
