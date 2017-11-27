package me.bananamilkshake.mongo.service;

import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.exception.CollectionAlreadyExistsException;
import me.bananamilkshake.mongo.service.index.IndexSetupService;
import me.bananamilkshake.mongo.service.validation.ValidationSetupService;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreatorService {

	private final ValidationSetupService validationSetupService;
	private final IndexSetupService indexSetupService;

	private final MongoTemplate mongoTemplate;

	public void create(String type, String validation, String index) {
		try {
			mongoTemplate.createCollection(type);
		} catch (UncategorizedMongoDbException uncategorizedMongoDbException) {
			throw new CollectionAlreadyExistsException(uncategorizedMongoDbException);
		}

		try {
			validationSetupService.setupValidation(mongoTemplate, type, validation);
			indexSetupService.setupIndex(mongoTemplate, type, index);
		} catch (Exception exception) {
			mongoTemplate.dropCollection(type);
			throw exception;
		}
	}
}
