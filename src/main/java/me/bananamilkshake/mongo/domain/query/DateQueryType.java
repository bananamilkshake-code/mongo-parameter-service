package me.bananamilkshake.mongo.domain.query;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

import static java.text.MessageFormat.format;

@AllArgsConstructor
public class DateQueryType {

	private final LocalDate localDate;

	@Override
	public String toString() {
		return format("ISODate('{0}')", localDate);
	}
}
