package me.bananamilkshake.mongo.controller;

import io.swagger.annotations.*;
import me.bananamilkshake.mongo.dto.ParameterDto;
import me.bananamilkshake.mongo.service.UploadService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

public interface ParameterController {

	@ApiResponses({
			@ApiResponse(code = 200, message = "Aggregated parameters of `type` for `user` found for appropriate `date`"),
			@ApiResponse(code = 204, message = "No values specified for `user` and `date` for parameter `type`"),
			@ApiResponse(code = 404, message = "Parameter `type` does not exists")
	})
	@GetMapping(params = "/{user}", produces = {MediaType.APPLICATION_JSON_VALUE})
	ResponseEntity<ParameterDto> getParameters(@PathVariable String type,
											   @PathVariable String user,
											   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime date);

	@ApiResponses({
			@ApiResponse(code = 201, message = "Parameter created successfully", responseHeaders = {@ResponseHeader(name = "Location", description = "Path to parameter to get")}),
			@ApiResponse(code = 400, message = "Description of index or validation is invalid"),
			@ApiResponse(code = 400, message = "Parameters `type` already exists")
	})
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	ResponseEntity createParameter(@PathVariable String type, @RequestBody String descriptionValue);

	@ApiResponses({
			@ApiResponse(code = 202, message = "Values uploaded successfully"),
			@ApiResponse(code = 404, message = "Parameter `type` does not exists"),
			@ApiResponse(code = 400, message = "Values description cannot be parsed")
	})
	@PostMapping(path = "/upload", consumes = {MediaType.APPLICATION_JSON_VALUE})
	ResponseEntity uploadValues(@PathVariable String type,
								@RequestParam String user,
								@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime validFrom,
								@RequestBody String values,
								@RequestParam(required = false, defaultValue = "INSERT") UploadService.UploadMode uploadMode);
}
