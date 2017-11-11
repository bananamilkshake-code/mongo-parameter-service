package me.bananamilkshake.mongo.service.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryCreatorTest {

	private AggregationFilterCreator aggregationFilterCreator;

	@Before
	public void setup() {
		final ObjectMapper objectMapper = new ObjectMapper();
		aggregationFilterCreator = new AggregationFilterCreator(objectMapper);
	}

	@Test
	public void shouldCreateCorrectFilterOnUserAndDate() {
		// given
		final String user = "RO";
		final LocalDateTime date = LocalDateTime.of(2017, 10, 7, 0, 0);

		// when
		final List<DBObject> result = aggregationFilterCreator.create(user, date);

		// then
		assertThat(result).hasSize(3);
	}
}