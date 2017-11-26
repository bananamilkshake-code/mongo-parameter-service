package me.bananamilkshake.mongo.service;

import com.mongodb.WriteResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bananamilkshake.mongo.domain.Fields;
import me.bananamilkshake.mongo.domain.Parameter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UploadService {

	private final MongoTemplate mongoTemplate;

	@AllArgsConstructor
	public enum UploadMode {
		INSERT {
			@Override
			public void upload(UploadService uploadService, String type, Parameter parameter) {
				uploadService.insert(type, parameter);
			}
		},
		REPLACE {
			@Override
			public void upload(UploadService uploadService, String type, Parameter parameter) {
				uploadService.replace(type, parameter);
			}
		};

		public abstract void upload(UploadService uploadService, String type, Parameter parameter);
	}

	private void insert(final String type, Parameter parameter) {
		mongoTemplate.insert(parameter, type);
		log.info("Inserted `{}` values into `{}` collection", parameter.getValues().size(), type);
	}

	private void replace(final String type, Parameter parameter) {
		WriteResult writeResult = mongoTemplate.remove(prepareQuery(parameter), type);
		log.info("Removed records from `{}` collection, result {}", type, writeResult);
		insert(type, parameter);
	}

	private Query prepareQuery(Parameter parameter) {
		Query query = new Query();
		query.addCriteria(Criteria.where(Fields.USER).is(parameter.getUser()));
		query.addCriteria(Criteria.where(Fields.VALID_FROM).is(parameter.getValidFrom()));
		return query;
	}
}
