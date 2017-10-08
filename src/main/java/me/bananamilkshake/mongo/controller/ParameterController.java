package me.bananamilkshake.mongo.controller;

import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.assembler.ParameterResponseAssembler;
import me.bananamilkshake.mongo.service.UploadService.UploadMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/parameter/{type}")
@AllArgsConstructor
public class ParameterController {

	private final ParameterCreationDescriptionParser parameterCreationDescriptionParser;
	private final ParameterResponseAssembler parameterResponseAssembler;

	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity getParameters(@PathVariable String type,
										@RequestParam String user,
										@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return parameterResponseAssembler.getParameters(type, user, date);
	}

	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity createParameter(@PathVariable String type,
										  @RequestBody String descriptionValue) {
		final ParameterCreationDescription description = parameterCreationDescriptionParser.parse(descriptionValue);
		return parameterResponseAssembler.createParameter(type, description.getValidation(), description.getIndex());
	}

	@PostMapping(path = "/upload", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity uploadParameters(@PathVariable String type,
										   @RequestBody String parameters,
										   @RequestParam(required = false, defaultValue = "INSERT_NEW") UploadMode uploadMode) {
		return parameterResponseAssembler.uploadParameters(type, parameters, uploadMode);
	}
}
