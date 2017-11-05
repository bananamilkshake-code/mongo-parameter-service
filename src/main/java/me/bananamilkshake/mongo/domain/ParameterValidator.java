package me.bananamilkshake.mongo.domain;

import me.bananamilkshake.mongo.domain.validation.TypeValidation;

public class ParameterValidator implements Parameter {

	@Override
	public TypeValidation getUser() {
		return TypeValidation.builder()
				.exists("true")
				.type("string")
				.<TypeValidation>build();
	}

	@Override
	public TypeValidation getValidFrom() {
		return TypeValidation.builder()
				.exists("true")
				.type("date")
				.<TypeValidation>build();
	}
}
