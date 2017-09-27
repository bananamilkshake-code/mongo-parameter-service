package me.bananamilkshake.mongo.domain.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@JsonInclude(NON_NULL)
public class QueryObject<Type> {

	@JsonProperty("$gte")
	private Type gte;

	@JsonProperty("$lte")
	private Type lte;
}
