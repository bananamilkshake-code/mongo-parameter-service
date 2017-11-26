package me.bananamilkshake.mongo.service;

import lombok.AllArgsConstructor;
import me.bananamilkshake.mongo.domain.Parameter;

public interface UploadService {

	@AllArgsConstructor
	enum UploadMode {
		INSERT {
			@Override
			public void upload(UploadService uploadService, String type, Parameter parameter) {
				uploadService.insert(type, parameter);
			}
		},
		REPLACE {
			@Override
			public void upload(UploadService uploadService, String type, Parameter parameter) {
				uploadService.replace(type, parameter);
			}
		};

		public abstract void upload(UploadService uploadService, String type, Parameter parameter);
	}

	void insert(String type, Parameter parameter);
	void replace(String type, Parameter parameter);
}
