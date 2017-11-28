package me.bananamilkshake.mongo.assembler;

import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.dto.ParameterDto;
import me.bananamilkshake.mongo.service.convert.ParameterConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParameterResponseAssemblerTest {

	@Mock
	private ParameterConverter parameterConverter;

	@Test
	public void should_return_OK_in_get_parameter_response() {

		// given
		Parameter parameter = new Parameter();
		ParameterDto parameterDto = givenParameterWillBeConvertedToDto(parameter);

		// when
		ResponseEntity<ParameterDto> responseEntity = parameterResponseAssembler().assembleGetParameterResponse(parameter);

		// then
		assertThat(responseEntity.getBody()).isEqualTo(parameterDto);
	}

	@Test
	public void should_return_CREATED_in_create_parameter_response() {

		// given
		String type = "Some_type";

		// when
		ResponseEntity responseEntity = parameterResponseAssembler().assembleCreateParameterResponse(type);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}

	@Test
	public void should_contain_link_to_parameter_in_create_parameter_response() {

		// given
		String type = "Some_type";

		// when
		ResponseEntity responseEntity = parameterResponseAssembler().assembleCreateParameterResponse(type);

		// then
		assertThat(responseEntity.getBody()).isEqualTo("/parameter/Some_type");
	}

	@Test
	public void should_return_ACCEPTED_in_upload_parameter_response() {

		// given

		// when
		ResponseEntity responseEntity = parameterResponseAssembler().assembleUploadValuesResponse();

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
	}

	private ParameterResponseAssembler parameterResponseAssembler() {
		return new ParameterResponseAssembler(parameterConverter);
	}

	private ParameterDto givenParameterWillBeConvertedToDto(Parameter parameter) {
		ParameterDto parameterDto = new ParameterDto();
		when(parameterConverter.convert(eq(parameter))).thenReturn(parameterDto);
		return parameterDto;
	}
}