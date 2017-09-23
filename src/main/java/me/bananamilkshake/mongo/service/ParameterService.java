package me.bananamilkshake.mongo.service;

public interface ParameterService {

	String getParameters(String type);

	void createParameter(String parameterMeta, String validation);

	void uploadParameters(String type, String parameters);
}
