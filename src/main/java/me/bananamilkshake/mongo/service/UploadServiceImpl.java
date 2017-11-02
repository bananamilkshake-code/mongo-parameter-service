package me.bananamilkshake.mongo.service;

import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.service.insert.InsertCommandCreator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class UploadServiceImpl implements UploadService {

	private final MongoTemplate mongoTemplate;
	private final InsertCommandCreator insertCommandCreator;

	@Override
	public void insert(final String type, String user, LocalDate validFrom, String values) {
		mongoTemplate.insert(insertCommandCreator.create(user, validFrom, values), type);
	}

	@Override
	public void cleanInsert(final String type, String user, LocalDate validFrom, String values) {
		mongoTemplate.findAllAndRemove(new Query(), type);
		insert(type, type, validFrom, values);
	}
}
