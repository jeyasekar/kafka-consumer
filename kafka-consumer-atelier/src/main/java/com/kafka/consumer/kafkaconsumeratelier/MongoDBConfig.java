package com.kafka.consumer.kafkaconsumeratelier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.kafka.consumer.mongodb.repository.WeatherRepository;
import com.mongodb.MongoClient;

@EnableMongoRepositories(basePackageClasses = WeatherRepository.class)
@Configuration
public class MongoDBConfig {
	@Value("${spring.data.mongodb.host}")
	private String host;
	@Value("${spring.data.mongodb.database}")
	private String db;

	@Bean
	public MongoClient mongo() {
		return new MongoClient(host);
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongo(), db);
	}
	/*
	 * @Bean CommandLineRunner commandLineRunner(WeatherRepository userRepository) {
	 * return strings -> { userRepository.save(new Weather());
	 * 
	 * 
	 * }; }
	 */
}
