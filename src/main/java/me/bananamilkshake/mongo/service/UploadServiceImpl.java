package me.bananamilkshake.mongo.service;

import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.domain.Fields;
import me.bananamilkshake.mongo.domain.Parameter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class UploadServiceImpl implements UploadService {

	private final MongoTemplate mongoTemplate;

	@Override
	public void insert(final String type, String user, LocalDate validFrom, List<Object> values) {
		mongoTemplate.insert(new Parameter<>(user, validFrom, values), type);
	}

	@Override
	public void replace(final String type, String user, LocalDate validFrom, List<Object> values) {
		mongoTemplate.remove(prepareQuery(user, validFrom), type);
		insert(type, type, validFrom, values);
	}

	private Query prepareQuery(String user, LocalDate validFrom) {
		Query query = new Query();
		query.addCriteria(Criteria.where(Fields.USER).is(user));
		query.addCriteria(Criteria.where(Fields.VALID_FROM).is(validFrom));
		return query;
	}
}
