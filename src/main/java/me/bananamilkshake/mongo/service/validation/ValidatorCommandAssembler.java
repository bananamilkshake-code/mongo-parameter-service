package me.bananamilkshake.mongo.service.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.domain.ParameterValidator;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static org.springframework.util.StringUtils.isEmpty;

@Component
@AllArgsConstructor
class ValidatorCommandAssembler {

	private final ObjectMapper objectMapper;

	String create(String typeName, String validationDescription) {
		try {
			return format("{ collMod: \"%s\", validator: { $and: [ %s %s ] } }",
					typeName, objectMapper.writeValueAsString(new ParameterValidator()), additionalDescription(validationDescription));
		} catch (JsonProcessingException jsonParsingException) {
			throw new RuntimeException("Failed to create JSON description from ParameterValidator object", jsonParsingException);
		}
	}

	private String additionalDescription(String validationDescription) {
		return isEmpty(validationDescription) ? "" : format(", %s", validationDescription);
	}
}
