package me.bananamilkshake.mongo.domain;

import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
public class Parameter<V> implements ParameterBase {

	private String user;
	private LocalDate validFrom;
	private List<V> values;
}
