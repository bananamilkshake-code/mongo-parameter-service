package me.bananamilkshake.mongo.service;

import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.domain.Parameter;

public interface UploadService {

	@AllArgsConstructor
	enum UploadMode {
		INSERT {
			@Override
			public void upload(UploadService uploadService, String type, Parameter<Object> parameter) {
				uploadService.insert(type, parameter);
			}
		},
		REPLACE {
			@Override
			public void upload(UploadService uploadService, String type, Parameter<Object> parameter) {
				uploadService.replace(type, parameter);
			}
		};

		public abstract void upload(UploadService uploadService, String type, Parameter<Object> parameter);
	}

	void insert(String type, Parameter<Object> parameter);
	void replace(String type, Parameter<Object> parameter);
}
