package me.bananamilkshake.mongo.service.index;

import org.springframework.data.mongodb.core.MongoTemplate;

public interface IndexSetupService {

	void setupIndex(MongoTemplate mongoTemplate, String type, String index);
}
