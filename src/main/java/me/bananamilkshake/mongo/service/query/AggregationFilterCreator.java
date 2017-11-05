package me.bananamilkshake.mongo.service.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.domain.ParameterAggregationFilter;
import me.bananamilkshake.mongo.domain.aggregation.match.MatchDescription;
import me.bananamilkshake.mongo.domain.aggregation.sort.SortDescription;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AggregationFilterCreator {

	private final ObjectMapper objectMapper;

	public List<DBObject> create(String user, LocalDate date) {
		return buildAggregationFilter(user, date)
				.stream()
				.map(this::convertFilterPart)
				.collect(Collectors.toList());
	}

	private ParameterAggregationFilter buildAggregationFilter(String user, LocalDate date) {
		return new ParameterAggregationFilter()
				.setMatch(new MatchDescription(user, date))
				.setSort(SortDescription.builder().validFrom(-1).build())
				.setLimit(1);
	}

	private DBObject convertFilterPart(Object object) {
		try {
			return BasicDBObject.parse(objectMapper.writeValueAsString(object));
		} catch (JsonProcessingException jsonProcessingException) {
			throw new RuntimeException("Could not deserialize query object to string", jsonProcessingException);
		}
	}
}
