package me.bananamilkshake.mongo.domain.aggregation.match.field;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static java.text.MessageFormat.format;

@AllArgsConstructor
public class DateField {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");

	private final LocalDateTime date;

	@JsonValue
	@JsonRawValue
	public String getValue() {
		return format("ISODate(\"{0}\")", FORMAT.format(convertToDate()));
	}

	private Date convertToDate() {
		return Date.from(date.toInstant(ZoneOffset.UTC));
	}
}
