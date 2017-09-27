package me.bananamilkshake.mongo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.domain.ParameterQuery;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
class QueryCreator {

	private final ObjectMapper objectMapper;

	BasicDBObject createDBObject(String user, LocalDate parameterDate) {
		return BasicDBObject.parse(createFilter(user, parameterDate));
	}

	private String createFilter(String user, LocalDate parameterDate) {
		try {
			return objectMapper.writeValueAsString(new ParameterQuery(user, parameterDate));
		} catch (JsonProcessingException jsonProcessingException) {
			throw new RuntimeException("Could not deserialize query object to string", jsonProcessingException);
		}
	}
}
