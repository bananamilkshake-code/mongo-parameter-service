package me.bananamilkshake.mongo.service;

import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public interface UploadService {

	@AllArgsConstructor
	enum UploadMode {
		INSERT {
			@Override
			public void upload(UploadService uploadService, String type, String user, LocalDate validFrom, List<Object> values) {
				uploadService.insert(type, user, validFrom, values);
			}
		},
		REPLACE {
			@Override
			public void upload(UploadService uploadService, String type, String user, LocalDate validFrom, List<Object> values) {
				uploadService.replace(type, user, validFrom, values);
			}
		};

		public abstract void upload(UploadService uploadService, String type, String user, LocalDate validFrom, List<Object> parameters);
	}

	void insert(String type, String user, LocalDate validFrom, List<Object> values);
	void replace(String type, String user, LocalDate validFrom, List<Object> values);
}
