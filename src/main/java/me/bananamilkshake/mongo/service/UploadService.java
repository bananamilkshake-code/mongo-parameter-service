package me.bananamilkshake.mongo.service;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

public interface UploadService {

	@AllArgsConstructor
	enum UploadMode {
		INSERT_NEW {
			@Override
			public void upload(UploadService uploadService, String type, String user, LocalDate validFrom, String values) {
				uploadService.insert(type, user, validFrom, values);
			}
		},
		CLEAN_INSERT {
			@Override
			public void upload(UploadService uploadService, String type, String user, LocalDate validFrom, String values) {
				uploadService.cleanInsert(type, user, validFrom, values);
			}
		};

		public abstract void upload(UploadService uploadService, String type, String user, LocalDate validFrom, String parameters);
	}

	void insert(String type, String user, LocalDate validFrom, String values);
	void cleanInsert(String type, String user, LocalDate validFrom, String values);
}
