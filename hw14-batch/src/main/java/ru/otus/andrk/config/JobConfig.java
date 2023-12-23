package ru.otus.andrk.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.andrk.listener.ChunkListenerImpl;
import ru.otus.andrk.listener.ItemReadListenerImpl;
import ru.otus.andrk.listener.ItemWriteListenerImpl;
import ru.otus.andrk.model.nosql.AuthorMongo;
import ru.otus.andrk.model.nosql.GenreMongo;
import ru.otus.andrk.model.sql.AuthorRdb;
import ru.otus.andrk.model.sql.GenreRdb;
import ru.otus.andrk.service.IdMappingService;
import ru.otus.andrk.service.TempColumnManageService;
import ru.otus.andrk.service.TransformIdService;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private static final int CHUNK_SIZE = 5;

    public static final String IMPORT_LIBRARY_JOB_NAME = "importLibraryJob";

    private final Logger logger = LoggerFactory.getLogger("Batch");

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final TransformIdService transformIdService;

    private final IdMappingService idMappingService;

    private final TempColumnManageService tempColumnManageService;



    @Bean
    public MongoItemReader<AuthorMongo> authorReader(MongoTemplate template) {
        return new MongoItemReaderBuilder<AuthorMongo>()
                .name("authorItemReader")
                .targetType(AuthorMongo.class)
                .template(template)
                .jsonQuery("{}")
                .sorts(Map.of())
                .build();
    }

    @Bean
    public MongoItemReader<GenreMongo> genreReader(MongoTemplate template) {
        return new MongoItemReaderBuilder<GenreMongo>()
                .name("genreItemReader")
                .targetType(GenreMongo.class)
                .template(template)
                .jsonQuery("{}")
                .sorts(Map.of())
                .build();
    }

    @Bean
    public ItemProcessor<AuthorMongo, AuthorRdb> authorProcessor() {
        return transformIdService::transform;
    }

    @Bean
    public ItemProcessor<GenreMongo, GenreRdb> genreProcessor() {
        return transformIdService::transform;
    }

    @Bean
    public JdbcBatchItemWriter<AuthorRdb> authorWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<AuthorRdb>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into authors (name, mongo_id) values (:name, :mongoId)")
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<GenreRdb> genreWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<GenreRdb>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into genres (name, mongo_id) values (:name, :mongoId)")
                .build();
    }

    @Bean
    public MethodInvokingTaskletAdapter createMongoIdsTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(tempColumnManageService);
        adapter.setTargetMethod("addMongoIds");
        return adapter;
    }


    public MethodInvokingTaskletAdapter fillIdTasklet(String entityName) {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(idMappingService);
        adapter.setTargetMethod("fillMap");
        adapter.setArguments(new Object[]{entityName});
        return adapter;
    }

    @Bean
    public MethodInvokingTaskletAdapter dropMongoIdsTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(tempColumnManageService);
        adapter.setTargetMethod("dropMongoIds");
        return adapter;
    }

    @Bean
    public Step createMongoIdsStep(MethodInvokingTaskletAdapter createMongoIdsTasklet) {
        return new StepBuilder("createMongoIds", jobRepository)
                .tasklet(createMongoIdsTasklet, platformTransactionManager)
                .build();
    }

    public Step fillIdStep(String entityName) {
        return new StepBuilder("fill_Id_"+ entityName, jobRepository)
                .tasklet(fillIdTasklet(entityName), platformTransactionManager)
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
    public Step dropMongoIdsStep(MethodInvokingTaskletAdapter dropMongoIdsTasklet) {
        return new StepBuilder("dropMongoIds", jobRepository)
                .tasklet(dropMongoIdsTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    public Job importLibraryJob(Step createMongoIdsStep, Step transformAuthorsStep,
                                Step transformGenresStep, Step dropMongoIdsStep) {
        return new JobBuilder(IMPORT_LIBRARY_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(createMongoIdsStep)
                .next(transformAuthorsStep)
                .next(fillIdStep("AUTHOR"))
                .next(transformGenresStep)
                .next(fillIdStep("GENRE"))
                .next(dropMongoIdsStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }

}
