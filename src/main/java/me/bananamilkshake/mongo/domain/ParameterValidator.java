package me.bananamilkshake.mongo.domain;

import me.bananamilkshake.mongo.domain.validation.TypeValidation;

public class ParameterValidator implements Parameter {

	public TypeValidation getUser() {
		return TypeValidation.builder()
				.exists("true")
				.type("string")
				.<TypeValidation>build();
	}

	public TypeValidation getValidFrom() {
		return TypeValidation.builder()
				.exists("true")
				.type("date")
				.<TypeValidation>build();
	}

	public TypeValidation getValidTo() {
		return TypeValidation.builder()
				.exists("true")
				.type("date")
				.<TypeValidation>build();
	}
}
