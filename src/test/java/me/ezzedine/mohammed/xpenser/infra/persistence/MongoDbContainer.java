package me.ezzedine.mohammed.xpenser.infra.persistence;

import org.testcontainers.containers.GenericContainer;

public class MongoDbContainer extends GenericContainer<MongoDbContainer> {

    private static MongoDbContainer instance;

    public MongoDbContainer() {
        super("mongo:latest");

        this.withExposedPorts(27017);
        this.withEnv("MONGO_INITDB_ROOT_USERNAME", "user");
        this.withEnv("MONGO_INITDB_ROOT_PASSWORD", "password");
    }

    @Override
    public void start() {
        super.start();

        System.setProperty("MONGO_DB_PORT", getMappedPort(27017).toString());
    }
}
