package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class TempColumnManageService {

    private final JdbcTemplate template;

    public void addMongoIds() {
        template.update("""
                alter table authors add column if not exists mongo_id varchar(200);
                alter table genres add column if not exists mongo_id varchar(200);
                """);
    }

    public void dropMongoIds(){
        template.update("""
                alter table authors drop column if exists mongo_id;
                alter table genres drop column if exists mongo_id;
                """);

    }
}
