package me.bananamilkshake.mongo.domain;

import lombok.Getter;
import me.bananamilkshake.mongo.domain.query.DateQueryType;
import me.bananamilkshake.mongo.domain.query.QueryObject;

import java.time.LocalDate;

@Getter
public class ParameterQuery implements Parameter {

	private String user;

	private QueryObject<DateQueryType> validFrom;
	private QueryObject<DateQueryType> validTo;

	public ParameterQuery(String user, LocalDate date) {
		this.user = user;
		setValidFrom(date);
		setValidTo(date);
	}

	private void setValidFrom(LocalDate value) {
		validFrom = QueryObject.<DateQueryType>builder().lte(new DateQueryType(value)).<DateQueryType>build();
	}

	private void setValidTo(LocalDate value) {
		validTo = QueryObject.<DateQueryType>builder().gte(new DateQueryType(value)).<DateQueryType>build();
	}
}
