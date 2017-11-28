package me.bananamilkshake.mongo.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.service.UploadService.UploadMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ParameterService {

	private final AggregationService aggregationService;
	private final CreatorService creatorService;
	private final UploadService uploadService;

	private final ParameterCreationDescriptionParser parameterCreationDescriptionParser;

	public Parameter getParameters(String type, String user, ZonedDateTime date) {
		return aggregationService.aggregate(type, user, date);
	}

	public void createParameter(String type, String descriptionValue) {
		final ParameterCreationDescription description = parameterCreationDescriptionParser.parse(descriptionValue);
		creatorService.create(type, description.getValidation(), description.getIndex());
	}

	public void uploadParameters(String type, String user, ZonedDateTime validFrom, String values, UploadMode uploadMode) {
		uploadService.upload(type, user, validFrom, values, uploadMode);
	}
}
