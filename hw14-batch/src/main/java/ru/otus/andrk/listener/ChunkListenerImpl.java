package ru.otus.andrk.listener;

import org.slf4j.Logger;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.lang.NonNull;

public class ChunkListenerImpl implements ChunkListener {

    private final Logger logger;

    public ChunkListenerImpl(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void beforeChunk(@NonNull ChunkContext chunkContext) {
        logger.info("Начало пачки");
    }

    @Override
    public void afterChunk(@NonNull ChunkContext chunkContext) {
        var stepContext = chunkContext.getStepContext();
        var countKey = stepContext.getStepExecutionContext()
                .keySet().stream().filter(r -> r.contains("read.count")).findFirst();
        var readCount = 0;
        if (countKey.isPresent()) {
            readCount = (Integer) stepContext.getStepExecutionContext().get(countKey.get());
        }
        logger.info("Конец пачки, размер: {}", readCount);
    }

    @Override
    public void afterChunkError(@NonNull ChunkContext chunkContext) {
        logger.info("Ошибка пачки");
    }
}
