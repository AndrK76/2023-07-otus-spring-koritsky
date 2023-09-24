package ru.otus.andrk.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;

@ChangeLog(order = "001")
public class InitDatabase {
    @ChangeSet(order = "000", id = "dropDB", author = "AndrK")
    public void dropDB(MongoDatabase database){
        System.out.println("\n\n!!!!!\n\n");
        database.drop();
    }

    @ChangeSet(order = "001", id = "testStep", author = "AndrK")
    public void addTestCollection(MongoDatabase database){
        database.createCollection("prodCol");
    }
}
