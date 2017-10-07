package me.bananamilkshake.mongo.service.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.domain.ParameterQuery;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class QueryCreator {

	private final ObjectMapper objectMapper;

	public BasicDBObject createDBObjectFilter(String user, LocalDate date) {
		final String filter = createFilter(user, date);
		return BasicDBObject.parse(filter);
	}

	private String createFilter(String user, LocalDate date) {
		try {
			final ParameterQuery parameterQuery = new ParameterQuery(user, date);
			return objectMapper.writeValueAsString(parameterQuery);
		} catch (JsonProcessingException jsonProcessingException) {
			throw new RuntimeException("Could not deserialize query object to string", jsonProcessingException);
		}
	}
}
