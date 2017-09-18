package me.bananamilkshake.mongo.assembler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bananamilkshake.mongo.service.ParameterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

import static java.text.MessageFormat.format;

@Slf4j
@Component
@AllArgsConstructor
public class ParameterResponseAssembler {

	private final ParameterService parameterService;

	public ResponseEntity getParameters(String name) {
		final String parameters = parameterService.getParameters(name);
		return new ResponseEntity<>(parameters, HttpStatus.OK);
	}

	public ResponseEntity createParameter(String type) {
		parameterService.createParameter(type);
		return ResponseEntity.created(createParameterUri(type)).build();
	}

	public ResponseEntity uploadParameters(String type, String parameters) {
		parameterService.uploadParameters(type, parameters);
		return new ResponseEntity(HttpStatus.ACCEPTED);
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
