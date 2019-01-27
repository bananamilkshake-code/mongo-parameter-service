package me.bananamilkshake.mongo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parameter implements ParameterBase {

	private String user;
	private LocalDateTime validFrom;
	private List<Map<String, Object>> values;
}
