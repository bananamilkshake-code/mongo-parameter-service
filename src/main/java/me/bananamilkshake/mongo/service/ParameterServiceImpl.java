package me.bananamilkshake.mongo.service;

import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bananamilkshake.mongo.exception.CollectionAlreadyExistsException;
import me.bananamilkshake.mongo.exception.NoSuchParameterExistsException;
import me.bananamilkshake.mongo.service.UploadService.UploadMode;
import me.bananamilkshake.mongo.service.index.IndexSetupService;
import me.bananamilkshake.mongo.service.query.QueryCreator;
import me.bananamilkshake.mongo.service.validation.ValidationSetupService;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ParameterServiceImpl implements ParameterService {

	private final MongoTemplate mongoTemplate;

	private final QueryCreator queryCreator;

	private final ValidationSetupService validationSetupService;
	private final IndexSetupService indexSetupService;

	private final UploadService uploadService;

	@Override
	public String getParameters(String type, String user, LocalDate date) {
		validateParameterType(type);
		return mongoTemplate.execute(type, collection -> {
			final BasicDBList dbList = new BasicDBList();
			try (DBCursor cursor = collection.find(queryCreator.createDBObjectFilter(user, date))) {
				while (cursor.hasNext()) {
					dbList.add(cursor.next());
				}
			}
			return dbList.toString();
		});
	}

	@Override
	public void createParameter(String type, String validation, String index) {
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

	@Override
	public void uploadParameters(String type, String parameters, UploadMode uploadMode) {
		validateParameterType(type);
		uploadMode.upload(uploadService, type, parameters);
	}

	private void validateParameterType(String type) {
		if (!mongoTemplate.collectionExists(type)) {
			throw new NoSuchParameterExistsException();
		}
	}
}
