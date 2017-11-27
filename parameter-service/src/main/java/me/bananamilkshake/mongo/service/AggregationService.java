package me.bananamilkshake.mongo.service;

import com.mongodb.AggregationOutput;
import com.mongodb.DBObject;
import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.exception.NoSuchParameterExistsException;
import me.bananamilkshake.mongo.service.query.AggregationFilterCreator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class AggregationService {

	private final AggregationFilterCreator aggregationFilterCreator;
	private final UtcConverter utcConverter;
	private final MongoTemplate mongoTemplate;

	public Parameter aggregate(String type, String user, ZonedDateTime date) {
		validateParameterType(type);
		return mongoTemplate.execute(type, collection -> {
			List<DBObject> aggregationFilter = aggregationFilterCreator.create(user, utcConverter.convertToUtc(date));
			AggregationOutput aggregationOutput = collection.aggregate(aggregationFilter);
			DBObject dbObject = StreamSupport
					.stream(aggregationOutput.results().spliterator(), false)
					.findFirst()
					.orElseThrow(NoSuchParameterExistsException::new);
			return mongoTemplate.getConverter().read(Parameter.class, dbObject);
		});
	}

	private void validateParameterType(String type) {
		if (!mongoTemplate.collectionExists(type)) {
			throw new NoSuchParameterExistsException();
		}
	}
}
