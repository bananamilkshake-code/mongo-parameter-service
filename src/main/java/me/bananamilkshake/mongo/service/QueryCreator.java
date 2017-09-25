package me.bananamilkshake.mongo.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static java.lang.String.format;

@Component
class QueryCreator {

	String create(String type, String user, LocalDate parameterDate) {
//		final DBObject validFromSelector = new BasicDBObject();
//		validFromSelector.put("$lte", Date.valueOf(parameterDate));
//		final String validFromMatcher = JSON.serialize(validFromSelector);
//
//		final DBObject validToSelector = new BasicDBObject();
//		validToSelector.put("gte", Date.valueOf(parameterDate));
//		final String validToMatcher = JSON.serialize(validToSelector);

		return format("{ find: \"%s\", filter: %s }", type, createFilter(user, parameterDate));
	}

	BasicDBObject createDBObject(String user, LocalDate parameterDate) {
		return BasicDBObject.parse(createFilter(user, parameterDate));
	}

	private String createFilter(String user, LocalDate parameterDate) {
		return format("{ user: \"%s\", validFrom: { '$lte': ISODate('%s') }, validTo: { '$gte': ISODate('%s') } }",
				user, parameterDate.toString(), parameterDate.toString());
	}
}
