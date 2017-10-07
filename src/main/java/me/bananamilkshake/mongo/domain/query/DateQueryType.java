package me.bananamilkshake.mongo.domain.query;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

import static java.text.MessageFormat.format;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@AllArgsConstructor
public class DateQueryType {

	private final LocalDate localDate;

	@JsonValue
	@JsonRawValue
	public String getValue() {
		return format("ISODate(\"{0}\")", localDate.format(ISO_LOCAL_DATE));
	}
}
