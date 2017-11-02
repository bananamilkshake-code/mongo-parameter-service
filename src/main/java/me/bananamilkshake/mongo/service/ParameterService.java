package me.bananamilkshake.mongo.service;

import me.bananamilkshake.mongo.service.UploadService.UploadMode;

import java.time.LocalDate;
import java.util.Set;

public interface ParameterService {

	Set<String> getTypes();

	String getParameters(String type, String user, LocalDate date);

	void createParameter(String parameterMeta, String validation, String index);

	void uploadParameters(String type, String user, LocalDate validFrom, String values, UploadMode uploadMode);
}
