package me.bananamilkshake.mongo.service.insert;

import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
public class InsertDescription {

	private String user;
	private LocalDate validFrom;
	private List<Object> values;
}
