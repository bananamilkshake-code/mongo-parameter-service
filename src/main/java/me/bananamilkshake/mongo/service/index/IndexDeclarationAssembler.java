package me.bananamilkshake.mongo.service.index;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import me.bananamilkshake.mongo.domain.index.IndexDescription;
import org.bson.BSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class IndexDeclarationAssembler {

	private BSONObject standardIndex;

	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper) {
		final String standardIndexDescription;
		try {
			standardIndexDescription = objectMapper.writeValueAsString(new IndexDescription());
		} catch (JsonProcessingException jsonProcessingException) {
			throw new RuntimeException("Failed to create JSON description from IndexDescription object", jsonProcessingException);
		}

		standardIndex = (BSONObject) JSON.parse(standardIndexDescription);
	}

	DBObject assemble(final String index) {
		final BasicDBObject dbObject = (BasicDBObject) JSON.parse(index);
		dbObject.putAll(standardIndex);
		return dbObject;
	}
}
