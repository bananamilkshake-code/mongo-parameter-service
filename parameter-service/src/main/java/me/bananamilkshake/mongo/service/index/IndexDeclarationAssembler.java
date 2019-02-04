package me.bananamilkshake.mongo.service.index;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.bananamilkshake.mongo.domain.index.IndexDescription;
import me.bananamilkshake.mongo.service.values.ValuesPreparationService;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.springframework.util.StringUtils.isEmpty;

@Component
@RequiredArgsConstructor
class IndexDeclarationAssembler {

	private final ValuesPreparationService valuesPreparationService;

	private BsonDocument standardIndex;

	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper) {
		final String standardIndexDescription;
		try {
			standardIndexDescription = objectMapper.writeValueAsString(new IndexDescription());
		} catch (JsonProcessingException jsonProcessingException) {
			throw new RuntimeException("Failed to create JSON description from IndexDescription object", jsonProcessingException);
		}

		standardIndex = BsonDocument.parse(standardIndexDescription);
	}

	Bson assemble(String valuesIndexDescription) {
		BsonDocument index = new BsonDocument();
		index.putAll(standardIndex);
		if (!isEmpty(valuesIndexDescription)) {
			index.putAll(valuesPreparationService.prepare(valuesIndexDescription));
		}
		return index;
	}
}
