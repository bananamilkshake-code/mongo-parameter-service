package me.bananamilkshake.mongo.service.validation;

import org.springframework.data.mongodb.core.MongoTemplate;

public interface ValidationSetupService {

	void setupValidation(MongoTemplate mongoTemplate, String parameterName, String validation);
}
