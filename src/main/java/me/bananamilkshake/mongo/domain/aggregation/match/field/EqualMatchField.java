package me.bananamilkshake.mongo.domain.aggregation.match.field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@AllArgsConstructor
@JsonInclude(NON_NULL)
public class EqualMatchField<Type> {

	@JsonProperty("$eq")
	private Type eq;
}
