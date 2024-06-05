package me.ezzedine.mohammed.xpenser.infra.config;

import com.mongodb.client.MongoClient;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventStoreConfiguration {

    @Bean
    public MongoTemplate axonMongoTemplate(MongoClient mongoClient) {
        return DefaultMongoTemplate.builder().mongoDatabase(mongoClient).build();
    }

    @Bean
    public EventStorageEngine eventStorageEngine(MongoTemplate axonMongoTemplate) {
        return MongoEventStorageEngine.builder()
                .mongoTemplate(axonMongoTemplate)
                .eventSerializer(JacksonSerializer.defaultSerializer())
                .snapshotSerializer(JacksonSerializer.defaultSerializer())
                .build();
    }

    @Bean
    public TokenStore tokenStore(MongoTemplate axonMongoTemplate) {
        return MongoTokenStore.builder()
                .mongoTemplate(axonMongoTemplate)
                .serializer(JacksonSerializer.defaultSerializer())
                .build();
    }
}
