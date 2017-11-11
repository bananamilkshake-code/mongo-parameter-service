package me.bananamilkshake.mongo.controller;

import lombok.extern.slf4j.Slf4j;
import me.bananamilkshake.mongo.assembler.ParameterResponseAssembler;
import me.bananamilkshake.mongo.service.UploadService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class ParameterParameterControllerTest {

	private ParameterController parameterController;

	@Mock
	private ParameterCreationDescriptionParser parameterCreationDescriptionParser;

	@Mock
	private ParameterResponseAssembler parameterResponseAssembler;

	@Before
	public void before() {
		parameterController = new ParameterController(parameterCreationDescriptionParser, parameterResponseAssembler);
	}

	@Test
	public void shouldReturnParametersForExistingType() {

		// given
		final String parameterName = "Flowers";
		final String parameters = "{ name: \"rose\", colour: \"yellow\" }";
		final String user = "BestFlowersInc";
		final ZonedDateTime parameterDate = ZonedDateTime.of(LocalDateTime.of(2017, 10, 7, 0, 0), ZoneId.of("UTC"));

		when(parameterResponseAssembler.getParameters(eq(parameterName), user, parameterDate)).thenReturn(ResponseEntity.ok(parameters));

		// when
		final ResponseEntity result = parameterController.getParameters(parameterName, user, parameterDate);

		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(parameters);
	}

	@Test
	public void shouldReturnUriToCreatedParameter() {

		// given
		final String newParameterName = "newParameter";
		final String pathToParameter = "/parameter/" + newParameterName;

		when(parameterResponseAssembler.createParameter(eq(newParameterName), eq(null), eq(null))).thenReturn(ResponseEntity.created(uri(pathToParameter)).build());

		// when
		final ResponseEntity result = parameterController.createParameter(newParameterName, null);

		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(result.getHeaders().getLocation().getPath()).isEqualTo(pathToParameter);
	}

	@Test
	public void shouldReturnAcceptedHttpStatusCodeOnSuccessfullParameterUpload() {

		// given
		final String parameterName = "Flowers";
		final String user = "Rose";
		final ZonedDateTime validFrom = ZonedDateTime.of(LocalDateTime.of(2017, 10, 7, 0, 0), ZoneId.of("UTC"));

		final String parameters = "[{ colour: \"yellow\" }]";
		UploadService.UploadMode uploadMode = UploadService.UploadMode.INSERT;

		when(parameterResponseAssembler.uploadParameters(eq(parameterName), user, validFrom, eq(parameters), uploadMode)).thenReturn(ResponseEntity.accepted().build());

		// when
		final ResponseEntity result = parameterController.uploadParameters(parameterName, user, validFrom, parameters, uploadMode);

		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		assertThat(result.getBody()).isNull();
	}

	private URI uri(String path) {
		try {
			return new URI(path);
		} catch (URISyntaxException exception) {
			throw new RuntimeException(exception);
		}
	}
}
