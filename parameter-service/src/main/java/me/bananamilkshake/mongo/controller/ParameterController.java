package me.bananamilkshake.mongo.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import me.bananamilkshake.mongo.dto.ParameterDto;
import me.bananamilkshake.mongo.service.UploadService;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;

public interface ParameterController {

	@ApiResponses({
			@ApiResponse(code = 200, message = "Aggregated parameters of `type` for `user` found for appropriate `date`"),
			@ApiResponse(code = 204, message = "No values specified for `user` and `date` for parameter `type`"),
			@ApiResponse(code = 404, message = "Parameter `type` does not exists")
	})
	ResponseEntity<ParameterDto> getParameters(String type, String user, ZonedDateTime date);

	@ApiResponses({
			@ApiResponse(code = 201, message = "Parameter created successfully", responseHeaders = {@ResponseHeader(name = "Location", description = "Path to parameter to get")}),
			@ApiResponse(code = 400, message = "Description of index or validation is invalid"),
			@ApiResponse(code = 400, message = "Parameters `type` already exists")
	})
	ResponseEntity createParameter(String type, String descriptionValue);

	@ApiResponses({
			@ApiResponse(code = 202, message = "Values uploaded successfully"),
			@ApiResponse(code = 404, message = "Parameter `type` does not exists"),
			@ApiResponse(code = 400, message = "Values description cannot be parsed")
	})
	ResponseEntity uploadValues(String type, String user, ZonedDateTime validFrom, String values, UploadService.UploadMode uploadMode);
}
