package me.bananamilkshake.mongo.service;

import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;
import com.mongodb.util.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bananamilkshake.mongo.exception.NoSuchParameterExistsException;
import me.bananamilkshake.mongo.service.validation.ValidationSetupService;
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

	@Override
	public String getParameters(String type, String user, LocalDate date) {
		validateParameterType(type);
		return mongoTemplate.execute(type, collection -> {
			final BasicDBList dbList = new BasicDBList();
			try (DBCursor cursor = collection.find(queryCreator.createDBObject(user, date))) {
				while (cursor.hasNext()) {
					dbList.add(cursor.next());
				}
			}
			return dbList.toString();
		});
	}

	@Override
	public void createParameter(String type, String validation) {
		mongoTemplate.createCollection(type);
		validationSetupService.setupValidation(mongoTemplate, type, validation);
	}

	@Override
	public void uploadParameters(String type, String parameters) {
		validateParameterType(type);
		mongoTemplate.insert((BasicDBList) JSON.parse(parameters), type);
	}

	private void validateParameterType(String type) {
		if (!mongoTemplate.collectionExists(type)) {
			throw new NoSuchParameterExistsException();
		}
	}
}
