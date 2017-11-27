package me.bananamilkshake.mongo.service;

import me.bananamilkshake.mongo.service.UploadService.UploadMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class ParameterServiceTest {

	@Mock
	private AggregationService aggregationService;

	@Mock
	private CreatorService creatorService;

	@Mock
	private UploadService uploadService;

	@Test
	public void shouldAcceptValidParameter() {

		// given
		String name = "someParameter";
		String user = "someUser";
		ZonedDateTime validFrom = ZonedDateTime.of(LocalDateTime.of(2017, 10, 7, 0, 0), ZoneId.of("UTC"));
		String parameters =
				"[" +
				"    {" +
				"        width: -1," +
				"        height: \"hello\"" +
				"    }" +
				"]";
		UploadMode uploadMode = UploadMode.INSERT;

		givenParametersAreValid(name, user, validFrom, parameters, uploadMode);

		// when
		Throwable thrown = catchThrowable(() -> parameterService().uploadParameters(name, user, validFrom, parameters, uploadMode));

		// then
		assertThat(thrown).isNull();
	}

	@Test
	public void shouldNotUploadInvalidParameters() {

		// given
		String name = "someParameter";
		String user = "someUser";
		ZonedDateTime validFrom = ZonedDateTime.of(LocalDateTime.of(2017, 10, 7, 0, 0), ZoneId.of("UTC"));
		String invalidParameters =
				"[" +
				"    {" +
				"        height: \"hello\"" +
				"    }" +
				"]";
		UploadMode uploadMode = UploadMode.INSERT;

		givenParametersAreInvalid(name, user, validFrom, invalidParameters, uploadMode);

		// when
		Throwable thrown = catchThrowable(() -> parameterService().uploadParameters(name, user, validFrom, invalidParameters, uploadMode));

		// then
		assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
	}

	private void givenParametersAreInvalid(String name, String user, ZonedDateTime validFrom, String parameters, UploadMode uploadMode) {
		doThrow(new DataIntegrityViolationException("Some exception"))
				.when(uploadService).upload(eq(name), eq(user), eq(validFrom), eq(parameters), eq(uploadMode));
	}

	private ParameterService parameterService() {
		return new ParameterService(aggregationService, creatorService, uploadService);
	}

	private void givenParametersAreValid(String name, String user, ZonedDateTime validFrom, String parameters, UploadMode uploadMode) {
		doNothing()
				.when(uploadService).upload(eq(name), eq(user), eq(validFrom), eq(parameters), eq(uploadMode));
	}
}
