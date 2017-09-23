package me.bananamilkshake.mongo.service.validation;

import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
class ValidatorCommandAssembler {

	String create(String typeName, String validationDescription) {
		return format("{collMod: \"%s\", validator: %s}", typeName, validationDescription);
	}
}
