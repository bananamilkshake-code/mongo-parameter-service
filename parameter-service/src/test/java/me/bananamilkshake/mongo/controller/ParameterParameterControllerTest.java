package me.bananamilkshake.mongo.controller;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import me.bananamilkshake.mongo.assembler.ParameterResponseAssembler;
import me.bananamilkshake.mongo.dto.ParameterDto;
import me.bananamilkshake.mongo.service.UploadService;
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
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class ParameterParameterControllerTest {

	@Mock
	private ParameterCreationDescriptionParser parameterCreationDescriptionParser;

	@Mock
	private ParameterResponseAssembler parameterResponseAssembler;

	@Test
	public void shouldReturnParametersForExistingType() {

		// given
		final String parameterName = "Flowers";
		final String user = "BestFlowersInc";
		final ZonedDateTime parameterDate = ZonedDateTime.of(LocalDateTime.of(2017, 10, 7, 0, 0), ZoneId.of("UTC"));

		ParameterDto parameter = givenParameterIsPresented(parameterName, user, parameterDate);

		// when
		final ResponseEntity result = parameterController().getParameters(parameterName, user, parameterDate);

		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualToComparingFieldByField(parameter);
	}

	@Test
	public void shouldReturnUriToCreatedParameter() {

		// given
		final String newParameterName = "newParameter";
		final String pathToParameter = "/parameter/" + newParameterName;

		givenValidParameterDescription(newParameterName, pathToParameter);

		// when
		final ResponseEntity result = parameterController().createParameter(newParameterName, null);

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

		givenValidParameterValuesToUpload();

		// when
		final ResponseEntity result = parameterController().uploadParameters(parameterName, user, validFrom, parameters, uploadMode);

		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		assertThat(result.getBody()).isNull();
	}

	private ParameterController parameterController() {
		return new ParameterController(parameterCreationDescriptionParser, parameterResponseAssembler);
	}

	private ParameterDto givenParameterIsPresented(String parameterName, String user, ZonedDateTime parameterDate) {
		Map<String, Object> values = ImmutableMap.<String, Object>builder()
				.put("value", 1)
				.build();

		final ParameterDto parameter = new ParameterDto(user, LocalDateTime.of(2017, 1, 1, 0, 0), newArrayList(values));

		when(parameterResponseAssembler.getParameters(eq(parameterName), eq(user), eq(parameterDate)))
				.thenReturn(ResponseEntity.ok(parameter));

		return parameter;
	}

	private void givenValidParameterDescription(String parameterName, String expectedParameterUri) {
		when(parameterCreationDescriptionParser.parse(any()))
				.thenReturn(new ParameterCreationDescription());

		when(parameterResponseAssembler.createParameter(eq(parameterName), eq(null), eq(null)))
				.thenReturn(ResponseEntity.created(uri(expectedParameterUri)).build());
	}

	private void givenValidParameterValuesToUpload() {
		when(parameterResponseAssembler.uploadParameters(any(), any(), any(), any(), any()))
				.thenReturn(ResponseEntity.accepted().build());
	}

	private URI uri(String path) {
		try {
			return new URI(path);
		} catch (URISyntaxException exception) {
			throw new RuntimeException(exception);
		}
	}
}
