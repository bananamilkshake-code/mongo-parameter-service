package me.bananamilkshake.mongo.service;

import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static java.lang.String.format;

@Component
class QueryCreator {

	BasicDBObject createDBObject(String user, LocalDate parameterDate) {
		return BasicDBObject.parse(createFilter(user, parameterDate));
	}

	private String createFilter(String user, LocalDate parameterDate) {
		return format("{ user: \"%s\", validFrom: { '$lte': ISODate('%s') }, validTo: { '$gte': ISODate('%s') } }",
				user, parameterDate.toString(), parameterDate.toString());
	}
}
