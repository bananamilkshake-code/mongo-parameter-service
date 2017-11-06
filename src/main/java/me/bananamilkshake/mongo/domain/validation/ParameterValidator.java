package me.bananamilkshake.mongo.domain.validation;

import me.bananamilkshake.mongo.domain.ParameterBase;

public class ParameterValidator implements ParameterBase {

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
