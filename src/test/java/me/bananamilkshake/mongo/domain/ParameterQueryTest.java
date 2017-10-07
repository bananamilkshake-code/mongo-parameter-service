package me.bananamilkshake.mongo.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ParameterQueryTest {

	@Test
	public void shouldBeSerializedToJson() throws JsonProcessingException {
		// given
		final String user = "RO";
		final LocalDate date = LocalDate.of(2017, 10, 7);
		final ObjectMapper objectMapper = new ObjectMapper();

		// when
		final String serialized = objectMapper.writeValueAsString(new ParameterQuery(user, date));

		// then
		final String expected = "{ \"user\" : \"RO\", \"validFrom\" : { \"$lte\" : ISODate(\"2017-10-07\" ) }, \"validTo\" : { \"$gte\" : ISODate(\"2017-10-07\" ) } }";
		assertThat(serialized).isEqualToIgnoringWhitespace(expected);
	}
}