package me.ezzedine.mohammed.xpenser.infra.config.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "me.ezzedine.mohammed.xpenser.infra")
public class MongoConfiguration /*extends AbstractReactiveMongoConfiguration*/ {

   /* @Bean
    public MongoClient mongoClient() {
        return MongoClients.create();
    }

    @Override
    protected @NonNull String getDatabaseName() {
        return "xpenser";
    }*/

//    @Bean
//    public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) {
//        return new ReactiveMongoTemplate(mongoClient, "xpenser");
//    }
}