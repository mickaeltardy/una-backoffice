package com.mtdev.una.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;

@Configuration
public class SpringMongoConfig {

	public @Bean MongoDbFactory mongoDbFactory() throws Exception {
		// return new SimpleMongoDbFactory(new MongoClient(), "test", new
		// UserCredentials("admin", "password"));
		return new SimpleMongoDbFactory(new MongoClient(), "una-database-test");
	}

	public @Bean MongoOperations mongoTemplate() throws Exception {

		MongoOperations mongoTemplate = new MongoTemplate(mongoDbFactory());

		return mongoTemplate;

	}

}