package me.bananamilkshake.mongo.service;

import lombok.AllArgsConstructor;

public interface UploadService {

	@AllArgsConstructor
	enum UploadMode {
		INSERT_NEW {
			@Override
			public void upload(UploadService uploadService, String type, String parameters) {
				uploadService.insert(type, parameters);
			}
		},
		CLEAN_INSERT {
			@Override
			public void upload(UploadService uploadService, String type, String parameters) {
				uploadService.cleanInsert(type, parameters);
			}
		};

		public abstract void upload(UploadService uploadService, String type, String parameters);
	}

	void insert(String type, String parameters);
	void cleanInsert(String type, String parameters);
}
