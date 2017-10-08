package me.bananamilkshake.mongo.service;

import com.mongodb.BasicDBList;
import com.mongodb.util.JSON;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UploadServiceImpl implements UploadService {

	private final MongoTemplate mongoTemplate;

	@Override
	public void insert(final String type, final String parameters) {
		mongoTemplate.insert((BasicDBList) JSON.parse(parameters), type);
	}

	@Override
	public void cleanInsert(final String type, final String parameters) {
		mongoTemplate.findAllAndRemove(new Query(), type);
		insert(type, parameters);
	}
}
