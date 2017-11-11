package me.bananamilkshake.mongo.domain.aggregation.match.field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@JsonInclude(NON_NULL)
public class ComparableMatchField<Type> {

	@JsonProperty("$gte")
	private Type gte;

	@JsonProperty("$lte")
	private Type lte;

	public ComparableMatchField<Type> gte(Type value) {
		this.gte = value;
		return this;
	}

	public ComparableMatchField<Type> lte(Type value) {
		this.lte = value;
		return this;
	}
}
