package me.bananamilkshake.mongo.service.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.bananamilkshake.mongo.domain.ParameterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static org.springframework.util.StringUtils.isEmpty;

@Component
class ValidatorCommandAssembler {

	private String standardValidation;

	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper) {
		try {
			standardValidation = objectMapper.writeValueAsString(new ParameterValidator());
		} catch (JsonProcessingException jsonParsingException) {
			throw new RuntimeException("Failed to create JSON description from ParameterValidator object", jsonParsingException);
		}
	}

	String create(String typeName, String validationDescription) {
		return format("{ collMod: \"%s\", validator: { $and: [ %s %s ] } }",
				typeName, standardValidation, additionalDescription(validationDescription));
	}

	private String additionalDescription(String validationDescription) {
		return isEmpty(validationDescription) ? "" : format(", %s", validationDescription);
	}
}
