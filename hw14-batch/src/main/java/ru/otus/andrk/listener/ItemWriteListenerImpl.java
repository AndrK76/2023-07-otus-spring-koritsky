package ru.otus.andrk.listener;

import org.slf4j.Logger;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.lang.NonNull;

public class ItemWriteListenerImpl<T> implements ItemWriteListener<T> {

    private final Logger logger;

    public ItemWriteListenerImpl(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void afterWrite(@NonNull Chunk<? extends T> items) {
        ItemWriteListener.super.afterWrite(items);
        logger.info("Конец записи, записано: {}",items.size());
    }

    @Override
    public void onWriteError(@NonNull Exception exception,@NonNull Chunk<? extends T> items) {
        ItemWriteListener.super.onWriteError(exception, items);
        logger.info("Ошибка записи");
    }

    @Override
    public void beforeWrite(@NonNull Chunk<? extends T> items) {
        ItemWriteListener.super.beforeWrite(items);
        logger.info("Начало записи");
    }
}
