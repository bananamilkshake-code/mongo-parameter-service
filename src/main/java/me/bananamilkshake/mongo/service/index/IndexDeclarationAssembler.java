package me.bananamilkshake.mongo.service.index;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import lombok.RequiredArgsConstructor;
import me.bananamilkshake.mongo.domain.index.IndexDescription;
import me.bananamilkshake.mongo.service.values.ValuesPreparationService;
import org.bson.BSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.springframework.util.StringUtils.isEmpty;

@Component
@RequiredArgsConstructor
class IndexDeclarationAssembler {

	private final ValuesPreparationService valuesPreparationService;

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

	DBObject assemble(String valuesIndexDescription) {
		BasicDBObject index = new BasicDBObject();
		index.putAll(standardIndex);
		if (!isEmpty(valuesIndexDescription)) {
			index.putAll(valuesPreparationService.prepare(valuesIndexDescription));
		}
		return index;
	}
}
