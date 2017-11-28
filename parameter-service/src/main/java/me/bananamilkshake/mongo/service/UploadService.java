package me.bananamilkshake.mongo.service;

import com.mongodb.WriteResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bananamilkshake.mongo.domain.Fields;
import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.exception.NoSuchParameterExistsException;
import me.bananamilkshake.mongo.service.values.ValuesParser;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class UploadService {

	@AllArgsConstructor
	public enum UploadMode {

		INSERT {
			@Override
			protected void upload(UploadService uploadService, String type, Parameter parameter) {
				uploadService.insert(type, parameter);
			}
		},

		REPLACE {
			@Override
			protected void upload(UploadService uploadService, String type, Parameter parameter) {
				uploadService.replace(type, parameter);
			}
		};

		protected abstract void upload(UploadService uploadService, String type, Parameter parameter);
	}

	private final ValuesParser valuesParser;
	private final UtcConverter utcConverter;
	private final MongoTemplate mongoTemplate;

	public void upload(String type, String user, ZonedDateTime validFrom, String values, UploadMode uploadMode) {
		validateParameterType(type);

		Parameter parameter = new Parameter(user, utcConverter.convertToUtc(validFrom), valuesParser.parse(values));
		uploadMode.upload(this, type, parameter);
	}

	private void validateParameterType(String type) {
		if (!mongoTemplate.collectionExists(type)) {
			throw new NoSuchParameterExistsException(type);
		}
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
