package me.bananamilkshake.mongo.service;

import com.mongodb.AggregationOutput;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.exception.CollectionAlreadyExistsException;
import me.bananamilkshake.mongo.exception.NoSuchParameterExistsException;
import me.bananamilkshake.mongo.service.UploadService.UploadMode;
import me.bananamilkshake.mongo.service.index.IndexSetupService;
import me.bananamilkshake.mongo.service.query.AggregationFilterCreator;
import me.bananamilkshake.mongo.service.validation.ValidationSetupService;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ParameterServiceImpl implements ParameterService {

	private final MongoTemplate mongoTemplate;

	private final AggregationFilterCreator aggregationFilterCreator;

	private final ValidationSetupService validationSetupService;
	private final IndexSetupService indexSetupService;

	private final UploadService uploadService;

	@Override
	public Parameter getParameters(String type, String user, ZonedDateTime date) {
		validateParameterType(type);
		return mongoTemplate.execute(type, collection -> {
			List<DBObject> aggregationFilter = aggregationFilterCreator.create(user, convertToUtc(date));
			AggregationOutput aggregationOutput = collection.aggregate(aggregationFilter);
			DBObject dbObject = StreamSupport
					.stream(aggregationOutput.results().spliterator(), false)
					.findFirst()
					.orElseThrow(NoSuchParameterExistsException::new);
			return mongoTemplate.getConverter().read(Parameter.class, dbObject);
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
	public void uploadParameters(String type,
								 String user,
								 ZonedDateTime validFrom,
								 String values,
								 UploadMode uploadMode) {
		validateParameterType(type);
		uploadMode.upload(uploadService, type, new Parameter(user, convertToUtc(validFrom), prepareValues(values)));
	}

	private void validateParameterType(String type) {
		if (!mongoTemplate.collectionExists(type)) {
			throw new NoSuchParameterExistsException();
		}
	}

	private List<Map<String, Object>> prepareValues(String values) {
		return (List<Map<String, Object>>) JSON.parse(values);
	}

	private LocalDateTime convertToUtc(ZonedDateTime zonedDateTime) {
		return zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}
