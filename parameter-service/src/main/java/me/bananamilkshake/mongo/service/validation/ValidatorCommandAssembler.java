package me.bananamilkshake.mongo.service.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import lombok.RequiredArgsConstructor;
import me.bananamilkshake.mongo.domain.validation.ParameterValidator;
import me.bananamilkshake.mongo.service.values.ValuesPreparationService;
import org.bson.BasicBSONObject;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static org.springframework.util.StringUtils.isEmpty;

@Component
@RequiredArgsConstructor
class ValidatorCommandAssembler {

	private final ValuesPreparationService valuesPreparationService;

	private String standardValidation;

	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper) {
		try {
			standardValidation = objectMapper.writeValueAsString(new ParameterValidator());
		} catch (JsonProcessingException jsonProcessingException) {
			throw new RuntimeException("Failed to create JSON description from ParameterValidator object", jsonProcessingException);
		}
	}

	String create(String typeName, String validationDescription) {
		return format("{ collMod: \"%s\", validator: { $and: [ %s %s ] } }",
				typeName, standardValidation, additionalDescription(validationDescription));
	}

	private String additionalDescription(String validationDescription) {
		return isEmpty(validationDescription) ? "" : format(", %s", prepareValidationDescription(validationDescription));
	}

	private String prepareValidationDescription(String validationDescription) {
		BasicDBObject bsonObject = new BasicDBObject();
		bsonObject.putAll(valuesPreparationService.prepare(validationDescription));
		return bsonObject.toJson();
	}
}
