package co.com.nequi.api.franchise_management_api.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "co.com.nequi.api.franchise_management_api.infrastructure.adapter.out.persistence.repository")
@EnableReactiveMongoAuditing
public class MongoConfig {
}
