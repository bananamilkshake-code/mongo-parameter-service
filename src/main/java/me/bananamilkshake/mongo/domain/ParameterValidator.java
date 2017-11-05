package me.bananamilkshake.mongo.domain;

import me.bananamilkshake.mongo.domain.validation.FieldValidator;

public class ParameterValidator implements Parameter {

	private static final String TYPE_STRING = "string";
	private static final String TYPE_DATE = "date";

	@Override
	public FieldValidator getUser() {
		return FieldValidator.builder()
				.exists("true")
				.type(TYPE_STRING)
				.build();
	}

	@Override
	public FieldValidator getValidFrom() {
		return FieldValidator.builder()
				.exists("true")
				.type(TYPE_DATE)
				.build();
	}
}
