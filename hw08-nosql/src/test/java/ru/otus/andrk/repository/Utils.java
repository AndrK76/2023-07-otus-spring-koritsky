package ru.otus.andrk.repository;

import lombok.Data;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import ru.otus.andrk.model.DatabaseSequence;

public class Utils {

    public static class MoreOneResultException extends RuntimeException{
    }

    @Data
    private static class ValueProjection {
        private Long value;
    }

    public static Long getSequenceValue(String sequenceName, MongoTemplate template) {
        var aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(sequenceName)),
                Aggregation.project().andExclude("_id").and("value"));
        var results = template
                .aggregate(aggregation, DatabaseSequence.class, ValueProjection.class)
                .getMappedResults();
        if (results.size() == 0) {
            return null;
        } else if (results.size() > 1) {
            throw new MoreOneResultException();
        } else {
            return results.get(0).getValue();
        }
    }
}
