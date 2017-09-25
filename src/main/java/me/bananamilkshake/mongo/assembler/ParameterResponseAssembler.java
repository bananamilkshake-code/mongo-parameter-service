package me.bananamilkshake.mongo.assembler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bananamilkshake.mongo.service.ParameterService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

import static java.text.MessageFormat.format;

@Slf4j
@Component
@AllArgsConstructor
public class ParameterResponseAssembler {

	private final ParameterService parameterService;

	public ResponseEntity getParameters(String name, String user, LocalDate date) {
		final String parameters = parameterService.getParameters(name, user, date);
		return ResponseEntity.ok(parameters);
	}

	public ResponseEntity createParameter(String type, String validation) {
		parameterService.createParameter(type, validation);
		return ResponseEntity.created(createParameterUri(type)).build();
	}

	public ResponseEntity uploadParameters(String type, String parameters) {
		parameterService.uploadParameters(type, parameters);
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
