package me.bananamilkshake.mongo.assembler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.dto.ParameterDto;
import me.bananamilkshake.mongo.service.convert.ParameterConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

import static java.text.MessageFormat.format;

@Slf4j
@Component
@AllArgsConstructor
public class ParameterResponseAssembler {

	private final ParameterConverter parameterConverter;

	public ResponseEntity<ParameterDto> assembleGetParameterResponse(Parameter parameter) {
		return ResponseEntity.ok(parameterConverter.convert(parameter));
	}

	public ResponseEntity assembleCreateParameterResponse(String type) {
		return ResponseEntity.created(createParameterUri(type)).build();
	}

	public ResponseEntity assembleUploadValuesResponse() {
		return ResponseEntity.accepted().build();
	}

	private static URI createParameterUri(String parameterName) {
		try {
			return new URI(format("/parameter/{0}", parameterName));
		} catch (URISyntaxException exception) {
			log.error("Could not create parameter URI", exception);
			return null;
		}
	}
}
