package me.bananamilkshake.mongo.controller;

import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.assembler.ParameterResponseAssembler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parameter/{type}")
@AllArgsConstructor
public class Controller {

	private final ParameterResponseAssembler parameterResponseAssembler;

	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity getParameters(@PathVariable String type) {
		return parameterResponseAssembler.getParameters(type);
	}

	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity createParameter(@PathVariable String type) {
		return parameterResponseAssembler.createParameter(type);
	}

	@PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity uploadParameters(@PathVariable String type, @RequestBody String parameters) {
		return parameterResponseAssembler.uploadParameters(type, parameters);
	}
}
