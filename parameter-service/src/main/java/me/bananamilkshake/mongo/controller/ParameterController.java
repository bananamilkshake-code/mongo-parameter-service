package me.bananamilkshake.mongo.controller;

import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.assembler.ParameterResponseAssembler;
import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.dto.ParameterDto;
import me.bananamilkshake.mongo.service.ParameterService;
import me.bananamilkshake.mongo.service.UploadService.UploadMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/parameter/{type}")
@AllArgsConstructor
public class ParameterController {

	private final ParameterService parameterService;
	private final ParameterResponseAssembler parameterResponseAssembler;

	@GetMapping(params = "/{user}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ParameterDto> getParameters(@PathVariable String type,
													  @PathVariable String user,
													  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime date) {
		Parameter parameter = parameterService.getParameters(type, user, date);
		return parameterResponseAssembler.assembleGetParameterResponse(parameter);
	}

	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity createParameter(@PathVariable String type,
										  @RequestBody String descriptionValue) {
		parameterService.createParameter(type, descriptionValue);
		return parameterResponseAssembler.assembleCreateParameterResponse(type);
	}

	@PostMapping(path = "/upload", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity uploadParameters(@PathVariable String type,
										   @RequestParam String user,
										   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime validFrom,
										   @RequestBody String values,
										   @RequestParam(required = false, defaultValue = "INSERT") UploadMode uploadMode) {
		parameterService.uploadParameters(type, user, validFrom, values, uploadMode);
		return parameterResponseAssembler.assembleUploadValuesResponse();
	}
}
