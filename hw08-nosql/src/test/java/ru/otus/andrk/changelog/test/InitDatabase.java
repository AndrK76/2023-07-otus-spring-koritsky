package ru.otus.andrk.changelog.test;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;

@ChangeLog(order = "001")
public class InitDatabase {
    @ChangeSet(order = "000", id = "dropDB", author = "AndrK", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "001", id = "testStep", author = "AndrK")
    public void addTestCollection(MongoDatabase database){
        database.createCollection("testCol");
    }
}

