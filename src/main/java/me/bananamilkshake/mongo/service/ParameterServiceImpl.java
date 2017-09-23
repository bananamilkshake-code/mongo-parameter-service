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

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ParameterServiceImpl implements ParameterService {

	private final MongoTemplate mongoTemplate;

	private final ValidationSetupService validationSetupService;

	@Override
	public String getParameters(String type) {
		if (!mongoTemplate.collectionExists(type)) {
			throw new NoSuchParameterExistsException();
		}

		return mongoTemplate.execute(type, collection -> {
			final BasicDBList dbList = new BasicDBList();
			try (DBCursor cursor = collection.find()) {
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
		final BasicDBList dbObject = (BasicDBList) JSON.parse(parameters);
		mongoTemplate.insert(dbObject, type);
	}
}
