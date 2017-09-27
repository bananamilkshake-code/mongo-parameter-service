package me.bananamilkshake.mongo.service.validation;

import com.mongodb.CommandResult;
import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.exception.IncorrectValidationDescriptionException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ValidationSetupServiceImpl implements ValidationSetupService {

	private final ValidatorCommandAssembler validatorCommandAssembler;

	@Override
	public void setupValidation(MongoTemplate mongoTemplate, String parameterName, String validationDescription) {
		final String validationCommand = validatorCommandAssembler.create(parameterName, validationDescription);
		final CommandResult result = mongoTemplate.executeCommand(validationCommand);
		if (!result.ok()) {
			throw new IncorrectValidationDescriptionException();
		}
	}
}
