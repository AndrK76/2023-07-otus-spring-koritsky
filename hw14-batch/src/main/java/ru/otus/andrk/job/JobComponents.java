package ru.otus.andrk.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.andrk.model.nosql.AuthorMongo;
import ru.otus.andrk.model.nosql.BookMongo;
import ru.otus.andrk.model.nosql.CommentMongo;
import ru.otus.andrk.model.nosql.GenreMongo;
import ru.otus.andrk.model.sql.AuthorRdb;
import ru.otus.andrk.model.sql.BookRdb;
import ru.otus.andrk.model.sql.CommentRdb;
import ru.otus.andrk.model.sql.GenreRdb;
import ru.otus.andrk.service.TransformIdService;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JobComponents {

    private final MongoTemplate mongoTemplate;

    private final TransformIdService transformIdService;

    private final DataSource rdbDataSource;


    @Bean
    @StepScope
    public MongoItemReader<AuthorMongo> authorReader() {
        return new MongoItemReaderBuilder<AuthorMongo>()
                .name("authorItemReader")
                .targetType(AuthorMongo.class)
                .template(mongoTemplate)
                .jsonQuery("{}")
                .sorts(Map.of())
                .build();
    }

    @Bean
    public ItemProcessor<AuthorMongo, AuthorRdb> authorProcessor() {
        return transformIdService::transform;
    }

    @Bean
    public JdbcBatchItemWriter<AuthorRdb> authorWriter() {
        return new JdbcBatchItemWriterBuilder<AuthorRdb>()
                .dataSource(rdbDataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into authors (name, mongo_id) values (:name, :mongoId)")
                .build();
    }


    @Bean
    @StepScope
    public MongoItemReader<GenreMongo> genreReader() {
        return new MongoItemReaderBuilder<GenreMongo>()
                .name("genreItemReader")
                .targetType(GenreMongo.class)
                .template(mongoTemplate)
                .jsonQuery("{}")
                .sorts(Map.of())
                .build();
    }

    @Bean
    public ItemProcessor<GenreMongo, GenreRdb> genreProcessor() {
        return transformIdService::transform;
    }

    @Bean
    public JdbcBatchItemWriter<GenreRdb> genreWriter() {
        return new JdbcBatchItemWriterBuilder<GenreRdb>()
                .dataSource(rdbDataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into genres (name, mongo_id) values (:name, :mongoId)")
                .build();
    }


    @Bean
    @StepScope
    public MongoItemReader<BookMongo> bookReader() {
        return new MongoItemReaderBuilder<BookMongo>()
                .name("bookItemReader")
                .targetType(BookMongo.class)
                .template(mongoTemplate)
                .jsonQuery("{}")
                .sorts(Map.of())
                .build();
    }

    @Bean
    public ItemProcessor<BookMongo, BookRdb> bookProcessor() {
        return transformIdService::transform;
    }

    @Bean
    public JdbcBatchItemWriter<BookRdb> bookWriter() {
        return new JdbcBatchItemWriterBuilder<BookRdb>()
                .dataSource(rdbDataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into books (name, mongo_id, author_id, genre_id) values " +
                        "(:name, :mongoId, :authorId, :genreId)")
                .build();
    }


    @Bean
    @StepScope
    public MongoItemReader<CommentMongo> commentReader() {
        return new MongoItemReaderBuilder<CommentMongo>()
                .name("commentItemReader")
                .targetType(CommentMongo.class)
                .template(mongoTemplate)
                .jsonQuery("{}")
                .sorts(Map.of())
                .build();
    }

    @Bean
    public ItemProcessor<CommentMongo, CommentRdb> commentProcessor() {
        return transformIdService::transform;
    }

    @Bean
    public JdbcBatchItemWriter<CommentRdb> commentWriter() {
        return new JdbcBatchItemWriterBuilder<CommentRdb>()
                .dataSource(rdbDataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into comments (text, book_id) values " +
                        "(:text, :bookId)")
                .build();
    }

}
