package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import static ru.otus.andrk.job.JobConstants.JOB_LOGGER_NAME;

@Service
@RequiredArgsConstructor
public class ClearDataService {

    private final Logger log = LoggerFactory.getLogger(JOB_LOGGER_NAME);

    private final JdbcTemplate template;

    @SuppressWarnings("unused")
    public void clearData(boolean clear) {
        if (clear) {
            log.info("Очищаем целевую базу данных!!!");
            template.update("""
                    delete from comments;
                    delete from books;
                    delete from authors;
                    delete from genres;
                    """);
        } else {
            log.info("Очистку базы не выполняем");
        }
    }
}
