package ru.otus.andrk.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DAO test content in csv resource")
public class ContentDaoCsvResourceImplTest {

    private ContentDao contentDao;

    @BeforeEach
    void initDao(){
        contentDao = new ContentDaoCsvResourceImpl("data.csv");
    }


    @Test
    void shouldCorrectReadDataFromResourceAndReturnNonEmptyList(){
        //TODO: это не пример теста, а "остаток" от разработки реализации ContentDaoCsvResourceImpl
        // просто потом повешено несколько assert, не пропадать же добру
        // пример теста в ....
        assertThat(contentDao.getQuestions())
                .isNotNull()
                .hasSize(5);
    }

}
