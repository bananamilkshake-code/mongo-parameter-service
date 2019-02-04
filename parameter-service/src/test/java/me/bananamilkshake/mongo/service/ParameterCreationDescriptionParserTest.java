package me.bananamilkshake.mongo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class ParameterCreationDescriptionParserTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	private ParameterCreationDescriptionParser parameterCreationDescriptionParser;

	private ObjectMapper objectMapper = new ObjectMapper();

	private static final String DESCRIPTION_INDEX = 
			"{\"name\":1}";

	private static final String DESCRIPTION_VALIDATION =
			"{\"name\":{\"$exists\":\"true\",\"$type\":\"string\"},\"value\":{\"$exists\":\"true\",\"$type\":\"number\",\"$gt\":0}}";

	@Before
	public void setup() {
		parameterCreationDescriptionParser = new ParameterCreationDescriptionParser(objectMapper);
	}

	@Test
	public void shouldReturnIndexAndValidationIfBothPresented() {
		// given
		final String description = format("{\"index\":%s,\"validation\":%s}", DESCRIPTION_INDEX, DESCRIPTION_VALIDATION);

		// when
		final ParameterCreationDescription result = parameterCreationDescriptionParser.parse(description);

		assertThat(result.getIndex()).isEqualTo(DESCRIPTION_INDEX);
		assertThat(result.getValidation()).isEqualTo(DESCRIPTION_VALIDATION);
	}

	@Test
	public void shouldReturnNullIndexIfNotPresented() {
		// given
		final String description = format("{\"validation\":%s}", DESCRIPTION_VALIDATION);

		// when
		final ParameterCreationDescription result = parameterCreationDescriptionParser.parse(description);

		assertThat(result.getIndex()).isNull();
		assertThat(result.getValidation()).isEqualTo(DESCRIPTION_VALIDATION);
	}

	@Test
	public void shouldReturnNullValidationIfNotPresented() {
		// given
		final String description = format("{\"index\":%s}", DESCRIPTION_INDEX);

		// when
		final ParameterCreationDescription result = parameterCreationDescriptionParser.parse(description);

		assertThat(result.getIndex()).isEqualTo(DESCRIPTION_INDEX);
		assertThat(result.getValidation()).isNull();
	}

	@Test
	public void shouldReturnNullIndexAndValidationIfBothNotPresented() {
		// given
		final String description = "{}";

		// when
		final ParameterCreationDescription result = parameterCreationDescriptionParser.parse(description);

		assertThat(result.getIndex()).isNull();
		assertThat(result.getValidation()).isNull();
	}
}