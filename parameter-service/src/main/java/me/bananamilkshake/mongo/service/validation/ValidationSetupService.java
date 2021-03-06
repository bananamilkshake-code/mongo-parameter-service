package me.bananamilkshake.mongo.service.validation;

import com.mongodb.CommandResult;
import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.exception.IncorrectValidationDescriptionException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ValidationSetupService {

	private final ValidatorCommandAssembler validatorCommandAssembler;

	public void setupValidation(MongoTemplate mongoTemplate, String parameterName, String validation) {
		final String validationCommand = validatorCommandAssembler.create(parameterName, validation);
		final CommandResult result = mongoTemplate.executeCommand(validationCommand);
		if (!result.ok()) {
			throw new IncorrectValidationDescriptionException();
		}
	}
}
