package me.bananamilkshake.mongo.service.validation;

import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.exception.IncorrectValidationDescriptionException;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ValidationSetupService {

	private final ValidatorCommandAssembler validatorCommandAssembler;

	public void setupValidation(MongoTemplate mongoTemplate, String parameterName, String validation) {
		final String validationCommand = validatorCommandAssembler.create(parameterName, validation);
		final Document result = mongoTemplate.executeCommand(validationCommand);
		if (!result.containsKey("ok")) {
			throw new IncorrectValidationDescriptionException();
		}
	}
}
