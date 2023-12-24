package ru.otus.andrk.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.andrk.job.JobTasklet;
import ru.otus.andrk.listener.ChunkListenerImpl;
import ru.otus.andrk.listener.ItemReadListenerImpl;
import ru.otus.andrk.listener.ItemWriteListenerImpl;
import ru.otus.andrk.listener.JobExecutionListenerImpl;
import ru.otus.andrk.model.nosql.AuthorMongo;
import ru.otus.andrk.model.nosql.BookMongo;
import ru.otus.andrk.model.nosql.CommentMongo;
import ru.otus.andrk.model.nosql.GenreMongo;
import ru.otus.andrk.model.sql.AuthorRdb;
import ru.otus.andrk.model.sql.BookRdb;
import ru.otus.andrk.model.sql.CommentRdb;
import ru.otus.andrk.model.sql.GenreRdb;

import static ru.otus.andrk.job.JobConstants.CHUNK_SIZE;
import static ru.otus.andrk.job.JobConstants.CLEAR_PARAMETER_NAME;
import static ru.otus.andrk.job.JobConstants.IMPORT_LIBRARY_JOB_NAME;
import static ru.otus.andrk.job.JobConstants.JOB_LOGGER_NAME;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final Logger logger = LoggerFactory.getLogger(JOB_LOGGER_NAME);

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final JobTasklet jobTasklet;


    @Bean
    public Job importLibraryJob(Step clearDataStep, Flow processAuthorAndGenreFlow,
                                Flow processBookFlow, Flow processCommentFlow) {
        return new JobBuilder(IMPORT_LIBRARY_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(createMongoIdsStep())
                .next(clearDataStep)
                .next(processAuthorAndGenreFlow)
                .next(processBookFlow)
                .next(processCommentFlow)
                .next(clearTransformMapStep())
                .next(dropMongoIdsStep())
                .end()
                .listener(new JobExecutionListenerImpl())
                .build();
    }

    @Bean
    public Flow processAuthorAndGenreFlow(Flow processAuthorFlow, Flow processGenreFlow) {
        return new FlowBuilder<SimpleFlow>("processAuthorAndGenreFlow")
                .split(taskJobExecutor())
                .add(processAuthorFlow, processGenreFlow)
                .build();
    }

    @Bean
    public Flow processAuthorFlow(Step transformAuthorsStep) {
        return new FlowBuilder<SimpleFlow>("processAuthorFlow")
                .start(transformAuthorsStep)
                .next(fillAuthorIdsStep())
                .build();
    }

    @Bean
    public Flow processGenreFlow(Step transformGenresStep) {
        return new FlowBuilder<SimpleFlow>("processAuthorFlow")
                .start(transformGenresStep)
                .next(fillGenreIdsStep())
                .build();
    }

    @Bean
    public Flow processBookFlow(Step transformBooksStep) {
        return new FlowBuilder<SimpleFlow>("processBookFlow")
                .start(transformBooksStep)
                .next(fillBookIdsStep())
                .build();
    }

    @Bean
    public Flow processCommentFlow(Step transformCommentsStep) {
        return new FlowBuilder<SimpleFlow>("processCommentFlow")
                .start(transformCommentsStep)
                .build();
    }

    @Bean
    @JobScope
    public Step clearDataStep(
            @Value("#{jobParameters['" + CLEAR_PARAMETER_NAME + "'] ?: true}") boolean clear) {
        return new StepBuilder("clearData", jobRepository)
                .tasklet(jobTasklet.getClearDataTasklet(clear), platformTransactionManager)
                .build();
    }

    @Bean
    public Step createMongoIdsStep() {
        return new StepBuilder("createMongoIds", jobRepository)
                .tasklet(jobTasklet.getCreateMongoIdsTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Step transformAuthorsStep(ItemReader<AuthorMongo> reader,
                                     ItemWriter<AuthorRdb> writer,
                                     ItemProcessor<AuthorMongo, AuthorRdb> itemProcessor) {
        return new StepBuilder("transformAuthorsStep", jobRepository)
                .<AuthorMongo, AuthorRdb>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemReadListenerImpl<>(logger))
                .listener(new ItemWriteListenerImpl<>(logger))
                .listener(new ChunkListenerImpl(logger))
                .build();
    }

    @Bean
    public Step fillAuthorIdsStep() {
        return new StepBuilder("fillAuthorIds", jobRepository)
                .tasklet(jobTasklet.getFillIdTasklet("AUTHOR"), platformTransactionManager)
                .build();
    }


    @Bean
    public Step transformGenresStep(ItemReader<GenreMongo> reader,
                                    ItemWriter<GenreRdb> writer,
                                    ItemProcessor<GenreMongo, GenreRdb> itemProcessor) {
        return new StepBuilder("transformGenresStep", jobRepository)
                .<GenreMongo, GenreRdb>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemReadListenerImpl<>(logger))
                .listener(new ItemWriteListenerImpl<>(logger))
                .listener(new ChunkListenerImpl(logger))
                .build();
    }

    @Bean
    public Step fillGenreIdsStep() {
        return new StepBuilder("fillGenreIds", jobRepository)
                .tasklet(jobTasklet.getFillIdTasklet("GENRE"), platformTransactionManager)
                .build();
    }

    @Bean
    public Step transformBooksStep(ItemReader<BookMongo> reader,
                                   ItemWriter<BookRdb> writer,
                                   ItemProcessor<BookMongo, BookRdb> itemProcessor) {
        return new StepBuilder("transformBooksStep", jobRepository)
                .<BookMongo, BookRdb>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemReadListenerImpl<>(logger))
                .listener(new ItemWriteListenerImpl<>(logger))
                .listener(new ChunkListenerImpl(logger))
                .build();
    }

    @Bean
    public Step fillBookIdsStep() {
        return new StepBuilder("fillBookIds", jobRepository)
                .tasklet(jobTasklet.getFillIdTasklet("BOOK"), platformTransactionManager)
                .build();
    }

    @Bean
    public Step transformCommentsStep(ItemReader<CommentMongo> reader,
                                      ItemWriter<CommentRdb> writer,
                                      ItemProcessor<CommentMongo, CommentRdb> itemProcessor) {
        return new StepBuilder("transformCommentsStep", jobRepository)
                .<CommentMongo, CommentRdb>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemReadListenerImpl<>(logger))
                .listener(new ItemWriteListenerImpl<>(logger))
                .listener(new ChunkListenerImpl(logger))
                .build();
    }

    @Bean
    public Step clearTransformMapStep() {
        return new StepBuilder("clearTransformMap", jobRepository)
                .tasklet(jobTasklet.getClearMapsTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Step dropMongoIdsStep() {
        return new StepBuilder("dropMongoIds", jobRepository)
                .tasklet(jobTasklet.getDropMongoIdsTasklet(), platformTransactionManager)
                .build();
    }


    @Bean
    public TaskExecutor taskJobExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

}
