package me.bananamilkshake.mongo.domain;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class Parameter<V> implements ParameterBase {

	private String user;
	private LocalDateTime validFrom;
	private List<V> values;
}
