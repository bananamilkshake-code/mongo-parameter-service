package me.bananamilkshake.mongo.service;

import com.mongodb.client.AggregateIterable;
import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.exception.NoParameterWithSuchArgumentsExistsException;
import me.bananamilkshake.mongo.exception.NoSuchParameterExistsException;
import me.bananamilkshake.mongo.service.query.AggregationFilterCreator;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

@Service
@AllArgsConstructor
public class AggregationService {

	private final AggregationFilterCreator aggregationFilterCreator;
	private final UtcConverter utcConverter;
	private final MongoTemplate mongoTemplate;

	public Parameter aggregate(String type, String user, ZonedDateTime date) {
		validateParameterType(type);
		return mongoTemplate.execute(type, collection -> {
			List<Bson> aggregationFilter = aggregationFilterCreator.create(user, utcConverter.convertToUtc(date));
			AggregateIterable<Parameter> aggregateIterable = collection.aggregate(aggregationFilter, Parameter.class);
			List<Parameter> parameters = newArrayList(aggregateIterable);
			if (parameters.isEmpty()) {
				throw new NoParameterWithSuchArgumentsExistsException();
			}

			if (parameters.size() > 1) {
				throw new RuntimeException(String.format("Too many argument selected for type %s, user %s date %s (amount %d)", type, user, date, parameters.size()));
			}

			return parameters.get(0);
		});
	}

	private void validateParameterType(String type) {
		if (!mongoTemplate.collectionExists(type)) {
			throw new NoSuchParameterExistsException(type);
		}
	}
}
