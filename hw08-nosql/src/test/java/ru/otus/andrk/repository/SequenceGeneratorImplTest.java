package ru.otus.andrk.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.repository.util.SequenceGeneratorImpl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.andrk.repository.Utils.getSequenceValue;

@DataMongoTest
@ComponentScan(basePackageClasses = SequenceGeneratorImpl.class)
public class SequenceGeneratorImplTest {

    @Autowired
    MongoTemplate template;

    @Autowired
    SequenceGeneratorImpl generator;

    Lock lock = new ReentrantLock();

    @Test
    public void shouldReturnCorrectValueAndIncreaseSequence() {
        try {
            lock.lock();
            String existSequenceName = Author.SEQUENCE_NAME;

            var expectedValueBefore = getSequenceValue(existSequenceName, template);
            assert expectedValueBefore != null;

            var actualValue = generator.getValue(existSequenceName);
            assertThat(actualValue).isNotNull().isEqualTo(expectedValueBefore + 1L);

            var expectedValueAfter = getSequenceValue(existSequenceName, template);
            assert expectedValueAfter != null;
            assertThat(actualValue).isEqualTo(expectedValueAfter);
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void shouldReturn_1_AndCreateSequenceForNoExistSequence() {
        try {
            lock.lock();
            String noExistSequenceName = "NO_EXIST_SEQUENCE_NAME";

            var expectedValueBefore = getSequenceValue(noExistSequenceName, template);
            assert expectedValueBefore == null;

            var actualValue = generator.getValue(noExistSequenceName);
            assertThat(actualValue).isEqualTo(1L);

            var expectedValueAfter = getSequenceValue(noExistSequenceName, template);
            assertThat(expectedValueAfter).isNotNull().isEqualTo(actualValue);
        } finally {
            lock.unlock();
        }
    }


}
