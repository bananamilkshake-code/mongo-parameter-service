package me.bananamilkshake.mongo.service;

import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.service.UploadService.UploadMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParameterServiceTest {

	@Mock
	private AggregationService aggregationService;

	@Mock
	private CreatorService creatorService;

	@Mock
	private UploadService uploadService;

	@Mock
	private ParameterCreationDescriptionParser parameterCreationDescriptionParser;

	@Test
	public void shouldCallAggregationServiceToGetParameters() {

		// given
		String name = "someParameter";
		String user = "someUser";
		ZonedDateTime date = ZonedDateTime.of(LocalDateTime.of(2017, 10, 7, 0, 0), ZoneId.of("UTC"));

		givenAggregationServiceExpectsCall(name, user, date);

		// when
		parameterService().getParameters(name, user, date);

		// then
		thenAggregationServiceCalled(name, user, date);
	}

	@Test
	public void shouldCallCreatorServiceToCreateParameter() {

		// given
		String name = "someParameter";
		String description = "Some description";

		String validation = "Some validation description";
		String index = "Some index description";

		givenParameterCreationDescriptionParserWillParseDescriptionTo(description, validation, index);
		givenCreatorServiceExpectsCall(name, validation, index);

		// when
		parameterService().createParameter(name, description);

		// then
		thenCreatorServiceCalled(name, validation, index);
	}

	@Test
	public void shouldCallUploadServiceToUploadParameters() {

		// given
		String name = "someParameter";
		String user = "someUser";
		ZonedDateTime validFrom = ZonedDateTime.of(LocalDateTime.of(2017, 10, 7, 0, 0), ZoneId.of("UTC"));
		String parameters = "Some parameters descriptions";
		UploadMode uploadMode = UploadMode.INSERT;

		givenUploadServiceExpectsCall(name, user, validFrom, parameters, uploadMode);

		// when
		parameterService().uploadParameters(name, user, validFrom, parameters, uploadMode);

		// then
		thenUploadServiceCalled(name, user, validFrom, parameters, uploadMode);
	}

	private ParameterService parameterService() {
		return new ParameterService(aggregationService, creatorService, uploadService, parameterCreationDescriptionParser);
	}

	private void givenAggregationServiceExpectsCall(String name, String user, ZonedDateTime date) {
		doReturn(new Parameter()).when(aggregationService).aggregate(eq(name), eq(user), eq(date));
	}

	private void thenAggregationServiceCalled(String name, String user, ZonedDateTime date) {
		verify(aggregationService, times(1)).aggregate(eq(name), eq(user), eq(date));
	}

	private void givenParameterCreationDescriptionParserWillParseDescriptionTo(String description,
																			   String validation,
																			   String index) {
		when(parameterCreationDescriptionParser.parse(eq(description)))
				.thenReturn(new ParameterCreationDescription(validation, index));
	}

	private void givenCreatorServiceExpectsCall(String name, String validation, String index) {
		doNothing().when(creatorService).create(eq(name), eq(validation), eq(index));
	}

	private void thenCreatorServiceCalled(String name, String validation, String index) {
		verify(creatorService, times(1)).create(eq(name), eq(validation), eq(index));
	}

	private void thenUploadServiceCalled(String name, String user, ZonedDateTime validFrom, String parameters, UploadMode uploadMode) {
		verify(uploadService, times(1)).upload(eq(name), eq(user), eq(validFrom), eq(parameters), eq(uploadMode));
	}

	private void givenUploadServiceExpectsCall(String name, String user, ZonedDateTime validFrom, String parameters, UploadMode uploadMode) {
		doNothing()
				.when(uploadService).upload(eq(name), eq(user), eq(validFrom), eq(parameters), eq(uploadMode));
	}
}
