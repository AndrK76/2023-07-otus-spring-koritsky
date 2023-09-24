package ru.otus.andrk.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
public class TmpTest {

    @Test
    public void test(){
        System.out.println("\n\nTest\n");
    }
}
