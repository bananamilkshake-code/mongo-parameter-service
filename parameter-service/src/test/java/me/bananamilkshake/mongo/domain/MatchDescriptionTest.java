package me.bananamilkshake.mongo.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.bananamilkshake.mongo.domain.aggregation.match.MatchDescription;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchDescriptionTest {

	@Test
	public void shouldBeSerializedToJson() throws JsonProcessingException {

		// given
		String user = "RO";
		LocalDateTime date = LocalDateTime.of(2017, 10, 7, 0, 0);

		// when
		String serialized = new ObjectMapper().writeValueAsString(new MatchDescription(user, date));

		// then
		String formattedDate = convertToString(date);
		String expected = "{\"user\":{\"$eq\":\"RO\"},\"validFrom\":{\"$lte\":ISODate(\"" + formattedDate + "\")}}";
		assertThat(serialized).isEqualToIgnoringWhitespace(expected);
	}

	private String convertToString(LocalDateTime date) {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz").format(Date.from(date.toInstant(ZoneOffset.UTC)));
	}
}