package me.bananamilkshake.mongo.assembler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.dto.ParameterDto;
import me.bananamilkshake.mongo.service.ParameterService;
import me.bananamilkshake.mongo.service.UploadService.UploadMode;
import me.bananamilkshake.mongo.service.convert.ParameterConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;

import static java.text.MessageFormat.format;

@Slf4j
@Component
@AllArgsConstructor
public class ParameterResponseAssembler {

	private final ParameterService parameterService;
	private final ParameterConverter parameterConverter;

	public ResponseEntity<ParameterDto> getParameters(String name, String user, ZonedDateTime date) {
		Parameter parameter = parameterService.getParameters(name, user, date);
		return ResponseEntity.ok(parameterConverter.convert(parameter));
	}

	public ResponseEntity createParameter(String type, String validation, String index) {
		parameterService.createParameter(type, validation, index);
		return ResponseEntity.created(createParameterUri(type)).build();
	}

	public ResponseEntity uploadParameters(String type,
										   String user,
										   ZonedDateTime validFrom,
										   String values,
										   UploadMode uploadMode) {
		parameterService.uploadParameters(type, user, validFrom, values, uploadMode);
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
