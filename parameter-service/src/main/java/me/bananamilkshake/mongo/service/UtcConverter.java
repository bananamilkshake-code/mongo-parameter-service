package me.bananamilkshake.mongo.service;

import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class UtcConverter {

	@Setter
	private ZoneId currentSystemZone;

	@PostConstruct
	public void postConstruct() {
		this.setCurrentSystemZone(ZoneId.systemDefault());
	}

	public LocalDateTime convertToUtc(ZonedDateTime zonedDateTime) {
		return zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"))
							.toInstant()
							.atZone(currentSystemZone)
							.toLocalDateTime();
	}
}
