package me.bananamilkshake.mongo.service;

import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;
import com.mongodb.util.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ParameterServiceImpl implements ParameterService {

	private final MongoTemplate mongoTemplate;

	@Override
	public String getParameters(String type) {
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
	public void createParameter(String type) {
		mongoTemplate.createCollection(type);
	}

	@Override
	public void uploadParameters(String type, String parameters) {
		final BasicDBList dbObject = (BasicDBList) JSON.parse(parameters);
		mongoTemplate.insert(dbObject, type);
	}
}
