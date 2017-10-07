package me.bananamilkshake.mongo.service.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryCreatorTest {

	private QueryCreator queryCreator;

	@Before
	public void setup() {
		final ObjectMapper objectMapper = new ObjectMapper();
		queryCreator = new QueryCreator(objectMapper);
	}

	@Test
	public void shouldCreateCorrectFilterOnUserAndDate() {
		// given
		final String user = "RO";
		final LocalDate date = LocalDate.of(2017, 10, 7);

		// when
		final BasicDBObject result = queryCreator.createDBObjectFilter(user, date);

		// then
		final String expected = "{ \"user\" : \"RO\", \"validFrom\" : { \"$lte\" : { \"$date\" : 1507323600000 } }, \"validTo\" : { \"$gte\" : { \"$date\" : 1507323600000 } } }";
		assertThat(result.toJson()).isEqualToIgnoringWhitespace(expected);
	}
}