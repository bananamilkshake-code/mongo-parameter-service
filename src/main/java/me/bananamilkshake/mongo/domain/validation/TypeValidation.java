package me.bananamilkshake.mongo.domain.validation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@Builder
@JsonInclude(NON_NULL)
public class TypeValidation {

	@JsonProperty(value = "$exists")
	private String exists;

	@JsonProperty(value = "$type")
	private String type;
}
