package me.bananamilkshake.mongo.service.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import me.bananamilkshake.mongo.service.values.ValuesPreparationService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ValidatorCommandAssemblerTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private ValuesPreparationService valuesPreparationService;

	private static final String STANDARD_VALIDATION =
			"{" +
			"    user: {" +
			"        $exists: \"true\"," +
			"        $type: \"string\"" +
			"    }," +
			"    validFrom: {" +
			"        $exists: \"true\"," +
			"        $type: \"date\"" +
			"    }" +
			"}";

	@Before
	public void setup() throws JsonProcessingException {
		when(objectMapper.writeValueAsString(any())).thenReturn(STANDARD_VALIDATION);

		when(valuesPreparationService.prepare(any())).thenAnswer(invocation -> BasicDBObject.parse(invocation.getArgument(0)));
	}

	@Test
	public void shouldReturnStandardValidationOnNullAdditionalValidation() {

		// given
		final String name = "parameter";

		// then
		final String result = validatorCommandAssembler().create(name, null);

		// when
		final String expected = removeSpaces("{collMod:\"parameter\",validator:{$and:[" + STANDARD_VALIDATION + "]}}");
		assertThat(removeSpaces(result)).isEqualTo(expected);
	}

	@Test
	public void shouldReturnAttachedValidationOnNonNullValidation() {

		// given
		final String name = "parameter";
		final String validationDescription = "{ \"name\": { \"$exists\": \"true\" } }";

		// then
		final String result = validatorCommandAssembler().create(name, validationDescription);

		// when
		final String expected = removeSpaces("{collMod:\"parameter\",validator:{$and:[" + STANDARD_VALIDATION + "," + validationDescription + "]}}");
		assertThat(removeSpaces(result)).isEqualTo(expected);
	}

	private ValidatorCommandAssembler validatorCommandAssembler() {
		ValidatorCommandAssembler validatorCommandAssembler = new ValidatorCommandAssembler(valuesPreparationService);
		validatorCommandAssembler.setObjectMapper(objectMapper);
		return validatorCommandAssembler;
	}

	private String removeSpaces(String string) {
		return string.replaceAll(" ", "");
	}
}
