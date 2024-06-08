package me.ezzedine.mohammed.xpenser.infra.persistence;

import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@EnableReactiveMongoRepositories
@AutoConfigureDataMongo
public class DatabaseIntegrationTest {

    @Container
    private static final MongoDbContainer container = new MongoDbContainer();


}
