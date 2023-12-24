package ru.otus.andrk.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static ru.otus.andrk.job.JobConstants.JOB_LOGGER_NAME;

@Component
public class JobExecutionListenerImpl implements JobExecutionListener {

    private final Logger logger = LoggerFactory.getLogger(JOB_LOGGER_NAME);

    @Override
    public void beforeJob(@NonNull JobExecution jobExecution) {
        logger.info("Начало job");
    }

    @Override
    public void afterJob(@NonNull JobExecution jobExecution) {
        logger.info("Конец job");
    }
}
