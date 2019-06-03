package com.kafka.consumer.service;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.kafka.consumer.mongodb.repository.WeatherRepository;

@Service
public class ConsumerService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerService.class);

	private final CountDownLatch latch = new CountDownLatch(1);
	
	@Autowired
	private WeatherRepository userRepository;
	
	@Autowired
	private MongoTemplate template;
	public CountDownLatch getLatch(){
		
		return latch;
				
	}

	/*
	 * @KafkaListener(topics="outputtopic") public void receiveMessage(final String
	 * message) {
	 * 
	 * LOGGER.info("received message='{}'", message); latch.countDown();
	 * 
	 * }
	 */
	public void insert() {
		 WeatherMetrics dat=new WeatherMetrics();
		 dat.setCity("chennai");
		 HashMap<String, CityWeather> map = new HashMap<String, CityWeather>();
		 CityWeather city=new CityWeather();
		 city.setCurrTemp(80.78);
		 map.put("one", city);
		 upsert(dat);
	}
	 @KafkaListener(topics = "meenatopic")
	    public void receive(@Payload WeatherMetrics data,
	                        @Headers MessageHeaders headers) {
		 LOGGER.info("received DataModel='{}'", data);
		// userRepository.save(data);
		 template.save(data, "AllTemp");
		 upsert(data);
	        headers.keySet().forEach(key -> {
	        	LOGGER.info("{}: {}", key, headers.get(key));
	        });
	    }

	private void upsert(WeatherMetrics data) {
		Query query = new Query();
		query.addCriteria(Criteria.where("city").is(data.getCity()));

		WeatherMetrics weather = template.findOne(query, WeatherMetrics.class);
		Update update = new Update();
        update.set("map.currTemp", data.getTemp());
		template.upsert(query, update, weather.getClass(), "LatestTemp");
	}

}
	
	
