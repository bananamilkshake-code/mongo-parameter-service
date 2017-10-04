package me.bananamilkshake.mongo.service;

import java.time.LocalDate;

public interface ParameterService {

	String getParameters(String type, String user, LocalDate date);

	void createParameter(String parameterMeta, String validation, String index);

	void uploadParameters(String type, String parameters);
}
