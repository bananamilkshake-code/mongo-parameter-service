package me.bananamilkshake.mongo.service;

import me.bananamilkshake.mongo.domain.Parameter;
import me.bananamilkshake.mongo.service.UploadService.UploadMode;

import java.time.ZonedDateTime;

public interface ParameterService {

	Parameter getParameters(String type, String user, ZonedDateTime date);

	void createParameter(String parameterMeta, String validation, String index);

	void uploadParameters(String type, String user, ZonedDateTime validFrom, String values, UploadMode uploadMode);
}
